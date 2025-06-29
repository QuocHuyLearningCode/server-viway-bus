package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TicketDTO {
    @JsonProperty("code_ticket") // <===== Mã vé
    private String codeTicket;

    @JsonProperty("seat_code") // <===== Mã ghế
    private String seatNumber;

    @JsonProperty("fullname") // <===== Tên khách hàng
    private String passengerName;

    @JsonProperty("phone_number") // <===== Số điện thoại khách hàng
    private String passengerPhone;

    @JsonProperty("email") // <===== email
    private String passengerEmail;

    private double price; // <===== Giá vé

    @JsonProperty("qr_code") // <===== QR CODE
    private String qrCode;

    private String status; // <===== Trạng thái vé

    @JsonProperty("payment_time") // <===== Thời gian thanh toán
    private LocalDateTime paymentTime;

    @JsonProperty("pick_up_time") // <===== Giờ đón
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime pickUpTime;

    @JsonProperty("drop_off_time") // <===== Giờ trả
    @JsonFormat(pattern = "HH:mm dd/MM/yyyy")
    private LocalDateTime dropOffTime;

    @JsonProperty("pick_up_point") // <===== Điểm đón
    private Long pickUpPoint;

    @JsonProperty("drop_off_point") // <===== Điểm trả
    private Long dropOffPoint;

}
