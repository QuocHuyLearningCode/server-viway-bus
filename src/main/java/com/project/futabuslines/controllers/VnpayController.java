//package com.project.futabuslines.controllers;
//
//import com.project.futabuslines.services.VnpayService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/vnpay")
//@RequiredArgsConstructor
//public class VnpayController {
//    private final VnpayService service;
//
//    @PostMapping("/create")
//    public ResponseEntity<?> createPayment(@RequestBody Map<String, String> req, HttpServletRequest servletReq) {
//        String orderId = req.get("orderId");
//        long amount = Long.parseLong(req.get("amount"));
//        String ip = servletReq.getRemoteAddr();
//        String url = service.createPaymentUrl(orderId, amount, ip);
//        return ResponseEntity.ok(Map.of("paymentUrl", url));
//    }
//
//    @GetMapping("/return")
//    public ResponseEntity<?> paymentReturn(HttpServletRequest request) {
//        int result = service.verifyReturn(request);
//        return ResponseEntity.ok(Map.of("resultCode", result));
//    }
//
//}
