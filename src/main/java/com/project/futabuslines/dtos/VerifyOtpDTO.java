package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerifyOtpDTO {
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String otp;
}
