package com.project.futabuslines.controllers;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.project.futabuslines.dtos.BookingDTO;
import com.project.futabuslines.dtos.PaymentDTO;
import com.project.futabuslines.dtos.TicketDTO;
import com.project.futabuslines.enums.PaymentResult;
import com.project.futabuslines.models.*;
import com.project.futabuslines.repositories.SeatTripRepository;
import com.project.futabuslines.repositories.TicketRepository;
import com.project.futabuslines.response.TicketDetailsResponse;
import com.project.futabuslines.response.TicketResponse;
import com.project.futabuslines.services.EmailService;
import com.project.futabuslines.services.NotificationService;
import com.project.futabuslines.services.TicketService;
import com.project.futabuslines.services.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
@RestController
@RequestMapping("${api.prefix}/ticket")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TicketController {
    @Autowired
    private QRCodeGenerator qrCodeGenerator;
    private final TicketService ticketService;
    private final VnpayService vnpayService;
    private final TicketRepository ticketRepository;
    private final SeatTripRepository seatTripRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;

    // POST: http://localhost:8080/api/v1/ticket
    @PostMapping("")
    public ResponseEntity<?> createRoute(
            @Valid @RequestBody BookingDTO bookingDTO,
            HttpServletRequest request,
            BindingResult result
    ){
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            TicketDetailsResponse ticket = ticketService.createTicket(bookingDTO, request);
            return ResponseEntity.ok(ticket);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // POST: http://localhost:8080/api/v1/ticket/payment
    @PostMapping("/payment")
    public ResponseEntity<?> paymentTicket(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request) {
        try {
            Object result = ticketService.paymentTicket(paymentDTO, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: http://localhost:8080/api/v1/ticket/all-routes
    @GetMapping("/all-routes")
    public ResponseEntity<List<Ticket>> getAllRoutes(){
        List<Ticket> route = ticketService.getAllTicket();
        return ResponseEntity.ok(route);
    }

    // GET: http://localhost:8080/api/v1/ticket/qrcode/{code}
    @GetMapping(value = "/qrcode/{code}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQRCode(@PathVariable("code") String code) throws Exception {
        byte[] qrImage = qrCodeGenerator.generateQRCodeImageByte(code, 200, 200);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrImage);
    }

    // GET: http://localhost:8080/api/v1/ticket/vnpay-return
    @GetMapping("/vnpay-return")
    public ResponseEntity<String> handlePaymentReturn(HttpServletRequest request) throws Exception {
        VnpayService.PaymentResult result = vnpayService.verifyReturn(request);
        VnpayService.PaymentDetails details = vnpayService.getPaymentDetails(request);
        String orderId = details.getOrderId(); // hoặc request.getParameter("vnp_TxnRef");

        if (result == VnpayService.PaymentResult.SUCCESS) {
            Ticket ticket = ticketRepository.findByCodeTicket(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy vé đã tạo"));

            ticket.setStatus(TicketStatus.CONFIRMED);
            ticket.setPaymentTime(LocalDateTime.now());
            ticketRepository.save(ticket);

            List<String> seatCodes = Arrays.asList(ticket.getSeatCode().split(","));
            List<SeatTrip> bookedSeats = seatTripRepository.findByTripIdAndSeatCodeIn(
                    ticket.getTrip().getId(), seatCodes
            );
            for (SeatTrip seat : bookedSeats) {
                seat.setIsBooked(true);
                seat.setStatus(TicketStatus.CONFIRMED);
            }
//            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            BufferedImage qrImage = qrCodeGenerator.generateQRCodeImage(ticket.getCodeTicket());
            seatTripRepository.saveAll(bookedSeats);
            String html = String.format("""
    <!DOCTYPE html>
    <html lang="vi">
    <head>
        <meta charset="UTF-8">
        <style>
            body { font-family: Arial, sans-serif; color: #333; padding: 20px; }
            .container { border: 1px solid #ccc; padding: 20px; border-radius: 10px; }
            .success { color: green; font-weight: bold; font-size: 18px; }
            .section-title { background-color: #f3f3f3; padding: 10px; font-weight: bold; margin-top: 20px; }
            table { width: 100%%; border-collapse: collapse; margin-top: 10px; }
            td { padding: 8px; border-bottom: 1px solid #ddd; }
        </style>
    </head>
    <body>
    <div class="container">
        <p class="success">Thanh toán thành công!</p>
        <p>Mã thanh toán: <strong>%s</strong></p>

        <div class="section-title">Thông tin vé</div>
        <table>
            <tr><td>Mã vé:</td><td>%s</td></tr>
            <tr><td>Nhà xe:</td><td>%s</td></tr>
            <tr><td>Số ghế:</td><td>%s</td></tr>
            <tr><td>Tuyến:</td><td>%s → %s</td></tr>
            <tr><td>Ngày đi:</td><td>%s</td></tr>
            <tr><td>Giờ:</td><td>%s</td></tr>
        </table>

        <div class="section-title">Lộ trình</div>
        <table>
            <tr><td>Điểm đón:</td><td>%s</td></tr>
            <tr><td>Điểm trả:</td><td>%s</td></tr>
        </table>

        <div class="section-title">Hành khách</div>
        <table>
            <tr><td>Họ tên:</td><td>%s</td></tr>
            <tr><td>SĐT:</td><td>%s</td></tr>
        </table>

        <div class="section-title">Chi tiết giá</div>
        <table>
            <tr><td>Giá vé:</td><td>%,dđ</td></tr>
            <tr><td>Số lượng:</td><td>%d</td></tr>
            <tr><td><strong>Tổng cộng:</strong></td><td><strong>%,dđ</strong></td></tr>
        </table>

        <p style="font-size: 12px; color: gray; margin-top: 30px;">
            Đây là email tự động. Vui lòng không trả lời. Mọi thắc mắc vui lòng liên hệ bộ phận hỗ trợ.
        </p>
    </div>
    </body>
    </html>
""",
                    ticket.getCodeTicket(),                      // Mã thanh toán
                    ticket.getCodeTicket(),                      // Mã vé
                    ticket.getTrip().getRoute().getBus().getOperator(), // Nhà xe
                    ticket.getSeatCode(),                        // Số ghế
                    ticket.getTrip().getRoute().getOrigin(),     // Tuyến đi
                    ticket.getTrip().getRoute().getDestination(),// Tuyến đến
                    ticket.getTrip().getDepartureTime(),         // Ngày đi
                    ticket.getTrip().getTimeStart(),             // Giờ đi
                    ticket.getPickUpPoint(),                     // Điểm đón
                    ticket.getDropOffPoint(),                    // Điểm trả
                    ticket.getUser().getFullName(),                          // Họ tên
                    ticket.getPhoneNumber(),                     // SĐT
                    ticket.getPrice(),                           // Giá vé
                    seatCodes.size(),                       // Số lượng
                    ticket.getTotalMoney()
            );

            User user = ticket.getUser();
            notificationService.sendNotification(
                    user.getId(),
                    "Thanh toán đ " + ticket.getTotalMoney() + " mua vé VIWAY thành công. Mã vé: " + ticket.getCodeTicket(),
                    "Thanh toán thành công"
            );
            emailService.send(ticket.getEmail(), "Xác nhận đặt vé thành công", html,  qrImage);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "viway://payment/result?status=success")
                    .build();
        } else {
            Ticket ticket = ticketRepository.findByCodeTicket(orderId)
                    .orElse(null); // dùng orElse(null) để tránh lỗi nếu chưa tạo vé

            if (ticket != null) {
                List<String> seatCodes = Arrays.asList(ticket.getSeatCode().split(","));
                List<SeatTrip> bookedSeats = seatTripRepository.findByTripIdAndSeatCodeIn(
                        ticket.getTrip().getId(), seatCodes
                );
                for (SeatTrip seat : bookedSeats) {
                    seat.setStatus(TicketStatus.CANCELLED);
                }
                seatTripRepository.saveAll(bookedSeats);
                ticket.setStatus(TicketStatus.CANCELLED);
                ticketRepository.save(ticket);
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "viway://payment/result?status=fail")
                    .build();
        }
    }

    // GET: http://localhost:8080/api/v1/ticket/history/{userId}
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<TicketResponse>> getTicketHistory(@PathVariable Long userId) {
        List<TicketResponse> history = ticketService.getHistoryTicket(userId);
        return ResponseEntity.ok(history);
    }

    // GET: http://localhost:8080/api/v1/ticket/details{id}
    @GetMapping("/details/{id}")
    public ResponseEntity<TicketDetailsResponse> getTicketDetails(@PathVariable String id) {
        return ResponseEntity.ok(ticketService.getHistoryDetails(id));
    }

    // DELETE: http://localhost:8080/api/v1/ticket/cancel/{id}
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancelTicket(@PathVariable("id") String codeTicket) {
        try {
            Ticket ticket = ticketRepository.findByCodeTicket(codeTicket)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy vé với mã: " + codeTicket));

            if (ticket.getStatus() != TicketStatus.CONFIRMED) {
                return ResponseEntity.badRequest().body("Chỉ có thể huỷ vé đã được xác nhận.");
            }

            ticket.setStatus(TicketStatus.CANCELLED);
            ticket.setPaymentTime(null);
            ticketRepository.save(ticket);

            List<String> seatCodes = Arrays.asList(ticket.getSeatCode().split(","));
            List<SeatTrip> bookedSeats = seatTripRepository.findByTripIdAndSeatCodeIn(
                    ticket.getTrip().getId(), seatCodes
            );
            for (SeatTrip seat : bookedSeats) {
                seat.setIsBooked(false);
                seat.setStatus(TicketStatus.CANCELLED);
            }
            seatTripRepository.saveAll(bookedSeats);

            notificationService.sendNotification(
                    ticket.getUser().getId(),
                    "Bạn đã huỷ vé có mã: " + ticket.getCodeTicket(),
                    "Huỷ vé thành công"
            );

            emailService.send(
                    ticket.getEmail(),
                    "Huỷ vé thành công",
                    "Bạn đã huỷ vé mã " + ticket.getCodeTicket() + ". Cảm ơn bạn đã sử dụng dịch vụ.",
                    null
            );

            return ResponseEntity.ok("Huỷ vé thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi huỷ vé: " + e.getMessage());
        }
    }
}
