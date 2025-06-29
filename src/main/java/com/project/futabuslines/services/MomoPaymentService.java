//package com.project.futabuslines.services;
//
//import com.project.futabuslines.payloads.BookingRequest;
//import com.project.futabuslines.payloads.BookingResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Base64;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class MomoPaymentService {
//
//    private final MomoService momoService; // Service tạo URL thanh toán MoMo
//
//    public BookingResponse bookTicketAndCreateMomoPayment(BookingRequest req) {
//        // Giả lập tạo vé
//        long ticketId = (long) (Math.random() * 1000 + 50);
//
//        // Tạo QR giả lập (base64)
//        String qr = Base64.getEncoder().encodeToString(("TICKET-" + ticketId).getBytes());
//
//        // Tính giá (giả định)
//        long price = 320000;
//
//        // Tạo URL thanh toán qua MoMo
//        String paymentUrl = momoService.createPaymentUrl(
//                "TICKET" + ticketId,
//                price,
//                req.getFull_name(),
//                req.getPhone_number(),
//                req.getEmail()
//        );
//
//        return BookingResponse.builder()
//                .id(ticketId)
//                .fullname(req.getFull_name())
//                .phone_number(req.getPhone_number())
//                .email(req.getEmail())
//                .status("pending")
//                .route("Hồ Chí Minh - Phú Yên")
//                .total_seat(req.getSeat_code().size())
//                .seat_code(req.getSeat_code())
//                .full_time("07:30 30-06-2025")
//                .pickUpPoint(req.getPick_up_point())
//                .dropOffPoint(req.getDrop_off_point())
//                .price(price)
//                .qr_code("data:image/png;base64," + qr)
//                .paymentUrl(paymentUrl)
//                .build();
//    }
//}
