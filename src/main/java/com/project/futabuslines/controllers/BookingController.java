//package com.project.futabuslines.controllers;
//
//import com.project.futabuslines.services.MomoPaymentService;
//import com.project.futabuslines.payloads.BookingRequest;
//import com.project.futabuslines.payloads.BookingResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("${api.prefix}/booking")
//@RequiredArgsConstructor
//public class BookingController {
//
//    private final MomoPaymentService momoPaymentService;
//
//    @PostMapping("/momo")
//    public ResponseEntity<?> bookTicketWithMomo(@RequestBody BookingRequest request) {
//        try {
//            // Gọi service để xử lý đặt vé và tạo link thanh toán
//            BookingResponse response = momoPaymentService.bookTicketAndCreateMomoPayment(request);
//
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Đặt vé thất bại: " + e.getMessage());
//        }
//    }
//}
