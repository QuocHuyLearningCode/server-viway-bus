package com.project.futabuslines.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.project.futabuslines.dtos.BookingDTO;
import com.project.futabuslines.dtos.PaymentDTO;
import com.project.futabuslines.dtos.TripDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.RedirectToVnPayException;
import com.project.futabuslines.models.*;
import com.project.futabuslines.repositories.*;
import com.project.futabuslines.response.PaymentRedirectResponse;
import com.project.futabuslines.response.TicketDetailsResponse;
import com.project.futabuslines.response.TicketResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TicketService implements ITicketService {
    private final TripRepository tripRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SeatTripRepository seatTripRepository;
    private final SeatRepository seatRepository;
    private final RouteRepository routeRepository;
    private final VnpayService vnpayService;
    private final MomoService momoService;

    @Transactional
    @Override
    public TicketDetailsResponse createTicket(BookingDTO tripDTO, HttpServletRequest request) throws Exception {

        String clientIp = getClientIp(request);
        Trip trip = tripRepository.findById(tripDTO.getTripId()).orElseThrow(() -> new DataNotFoundException("Cannot find Bus with id: " + tripDTO.getTripId()));
        User user = userRepository.findById(tripDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("Cannot find Bus with id: " + tripDTO.getUserId()));
        String seatCode = String.join(",", tripDTO.getSeatNumbers());

        int available = trip.getAvailableSeats();
        int booked = tripDTO.getSeatNumbers().size();

        if (booked > available) {
            throw new IllegalStateException("❌ Không đủ ghế trống");
        }

        // Cập nhật số ghế còn lại
        trip.setAvailableSeats(available - booked);
        tripRepository.save(trip);



        Long routeId = trip.getRoute().getId(); // hoặc trip.getRouteId() nếu bạn dùng primitive
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tuyến đường"));

        Long busId = route.getBus().getId(); // hoặc route.getBusId()

// Lấy danh sách ghế thuộc loại xe đó
        List<Seat> seatsOfBus = seatRepository.findByBusId(busId);
        Set<String> validSeatCodes = seatsOfBus.stream()
                .map(Seat::getSeatCode)
                .collect(Collectors.toSet());

// So sánh ghế người dùng gửi có hợp lệ không
        List<String> userSeats = tripDTO.getSeatNumbers();

        boolean allValid = userSeats.stream().allMatch(validSeatCodes::contains);

        if (!allValid) {
            throw new IllegalArgumentException("❌ Một số ghế không tồn tại trên loại xe này");
        }
        List<SeatTrip> existingSeats = seatTripRepository
                .findByTripIdAndSeatCodeIn(tripDTO.getTripId(), tripDTO.getSeatNumbers());

// Lọc ra ghế đã được đặt
        List<String> alreadyBookedSeats = existingSeats.stream()
                .filter(seat -> seat.getIsBooked() || TicketStatus.PENDING.equals(seat.getStatus()))
                .map(SeatTrip::getSeatCode)
                .toList();

        if (!alreadyBookedSeats.isEmpty()) {
            throw new IllegalStateException("⚠️ Các ghế sau đã được đặt hoặc đang chờ thanh toán: " + String.join(", ", alreadyBookedSeats));
        }
        List<SeatTrip> newSeats = new ArrayList<>();

        for (String seatCodeString : tripDTO.getSeatNumbers()) {
            SeatTrip seatTrip = SeatTrip.builder()
                    .seatCode(seatCodeString)
                    .isBooked(false)
                    .status(TicketStatus.PENDING)
                    .trip(trip)
                    .build();
            newSeats.add(seatTrip);
        }
        seatTripRepository.saveAll(newSeats);
        LocalDateTime tripDate = trip.getDepartureTime().atStartOfDay();

        String codeTicket = generateUniqueTicketCode();
            String qrCode = generateQRCodeBase64(codeTicket);

            Ticket newTicket = Ticket.builder()
                    .trip(trip)
                    .user(user)
                    .seatCode(seatCode)
                    .codeTicket(codeTicket)
                    .fullname(tripDTO.getPassengerName())
                    .phoneNumber(tripDTO.getPassengerPhone())
                    .email(tripDTO.getPassengerEmail())
                    .price(trip.getPrice())
                    .totalMoney(booked * trip.getPrice())
                    .pickUpTime(tripDate)
                    .qrCode(qrCode)
                    .pickUpPoint(tripDTO.getPickUpPoint())
                    .dropOffPoint(tripDTO.getDropOffPoint())
                    .status(TicketStatus.PENDING)
                    .paymentTime(null)
                    .requireShuttle(tripDTO.isRequireShuttle())
                    .build();
            ticketRepository.save(newTicket);
        System.out.println("Thời gian tạo vé: " + LocalDateTime.now());

//        // Nếu là VNPAY → trả URL thanh toán
//        if (tripDTO.getPaymentMethod().equalsIgnoreCase("VNPAY")) {
//            String paymentUrl = vnpayService.createPaymentUrl(codeTicket, newTicket.getTotalMoney(), clientIp);
//            // Có thể return URL này trong response nếu bạn dùng DTO trả ra client
//            throw new RedirectToVnPayException(paymentUrl); // tuỳ bạn xử lý dạng response hay exception redirect
//        }
        return TicketDetailsResponse.fromTicket(newTicket);
        }
    public Object paymentTicket(PaymentDTO paymentDTO, HttpServletRequest request) {
        String clientIp = getClientIp(request);

        Ticket ticket = ticketRepository.findById(paymentDTO.getTicketId())
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy vé với ID đã cung cấp"));

        String codeTicket = ticket.getCodeTicket();
        long totalMoney = ticket.getTotalMoney(); // giả sử trả về long
        int amount = (int) totalMoney; // Đơn vị: đồng

        if (paymentDTO.getPaymentMethod().equalsIgnoreCase("VNPAY")) {
            String paymentUrl = vnpayService.createPaymentUrl(codeTicket, amount, clientIp);
            return new PaymentRedirectResponse(paymentUrl, "✅ Vui lòng truy cập đường dẫn VNPAY để thanh toán.");
        }

        if (paymentDTO.getPaymentMethod().equalsIgnoreCase("MOMO")) {
            String paymentUrl = momoService.createPaymentUrl(codeTicket, amount, clientIp);
            return new PaymentRedirectResponse(paymentUrl, "✅ Vui lòng truy cập đường dẫn MoMo để thanh toán.");
        }

        return ticket;
    }

    @Override
    public List<Ticket> getAllTicket() {
        return ticketRepository.findAll();
    }

    private String generateUniqueTicketCode() {
        String code;
        do {
            code = "VIWAY" + generateRandomAlphaNumeric(6); // 6 ký tự chữ và số
        } while (ticketRepository.existsByCodeTicket(code));
        return code;
    }

    private String generateRandomAlphaNumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


    private String generateQRCodeBase64(String content) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", outputStream);
        byte[] qrBytes = outputStream.toByteArray();

        // Add this line: prefix required for rendering in HTML <img src="...">
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrBytes);
    }
    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return forwarded != null ? forwarded.split(",")[0] : request.getRemoteAddr();
    }

    public List<TicketResponse> getHistoryTicket(Long userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);

        return tickets.stream()
                .filter(ticket -> {
                    String status = ticket.getStatus();
                    return TicketStatus.CONFIRMED.equalsIgnoreCase(status)
                            || TicketStatus.CANCELLED.equalsIgnoreCase(status);
                })
                .map(TicketResponse::fromTicket)
                .collect(Collectors.toList());
    }

    public TicketDetailsResponse getHistoryDetails(String id) {
        Ticket ticket = ticketRepository.findByCodeTicket(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé với ID: " + id));

        return TicketDetailsResponse.fromTicket(ticket);
    }

    @Scheduled(fixedRate = 60000)
    public void cancelUnpaidTickets() {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1).withNano(0);

        List<Ticket> tickets = ticketRepository.findAllByStatusAndCreatedAtBefore(TicketStatus.PENDING, oneMinuteAgo);

        for (Ticket ticket : tickets) {
            // Lấy danh sách mã ghế từ chuỗi
            String seatCodeStr = ticket.getSeatCode(); // ví dụ: "A1,A2"
            List<String> seatCodes = Arrays.asList(seatCodeStr.split(","));

            // Xoá các ghế đã đặt
            List<SeatTrip> bookedSeats = seatTripRepository.findByTripIdAndSeatCodeIn(
                    ticket.getTrip().getId(), seatCodes
            );
            seatTripRepository.deleteAll(bookedSeats);

            // Huỷ vé
            ticket.setStatus(TicketStatus.CANCELLED);

            // Cập nhật lại số ghế trống của chuyến đi
            Trip trip = ticket.getTrip();
            int currentAvailable = trip.getAvailableSeats();
            int seatsToRestore = seatCodes.size();
            trip.setAvailableSeats(currentAvailable + seatsToRestore);
            tripRepository.save(trip);
        }

        ticketRepository.saveAll(tickets);

        if (!tickets.isEmpty()) {
            System.out.println("✅ Đã huỷ " + tickets.size() + " vé chưa thanh toán sau 1 phút.");
        }
    }





}


