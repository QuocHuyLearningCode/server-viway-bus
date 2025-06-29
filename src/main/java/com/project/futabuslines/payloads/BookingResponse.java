package com.project.futabuslines.payloads;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private String fullname;
    private String phone_number;
    private String email;
    private String status;
    private String route;
    private int total_seat;
    private List<String> seat_code;
    private String full_time;
    private String pickUpPoint;
    private String dropOffPoint;
    private long price;
    private String qr_code;
    private String paymentUrl; // Đường dẫn đến trang thanh toán MoMo
}
