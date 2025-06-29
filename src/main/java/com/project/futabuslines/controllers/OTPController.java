package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.OTPRequest;
import com.project.futabuslines.dtos.OTPResetPasswordDTO;
import com.project.futabuslines.dtos.OtpVerificationRequest;
import com.project.futabuslines.dtos.UserSummaryDTO;
import com.project.futabuslines.response.OTPResponse;
import com.project.futabuslines.services.OTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;

    // POST: http://localhost:8080/api/v1/send
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody OTPRequest contact) {
        try {
            Object result = otpService.sendOtp(contact.getContact());

            if (result instanceof UserSummaryDTO) {
                return ResponseEntity.ok(result); // Trả về thông tin người dùng đã tồn tại
            } else if (result instanceof String) {
                return ResponseEntity.ok(result); // Trả về thông báo đã gửi OTP
            } else {
                return ResponseEntity.status(500).body("Unexpected response");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST: http://localhost:8080/api/v1/reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<?> sendOtpResetPassWord(@RequestBody OTPResetPasswordDTO contact) {
        try {
            String result = otpService.sendOtpForResetPassword(contact.getContact());
                return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST: http://localhost:8080/api/v1/verify
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
        OTPResponse response = otpService.verifyOtp(request.getContact(), request.getOtp());
        return ResponseEntity.ok(response);
    }
}
