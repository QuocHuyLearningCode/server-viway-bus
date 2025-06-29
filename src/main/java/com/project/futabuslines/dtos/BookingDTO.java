package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {

    @JsonProperty("trip_id")
    @NotNull(message = "ID chuyến xe không được để trống")
    private Long tripId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("seat_code")
    @NotEmpty(message = "Danh sách ghế không được để trống")
    private List<String> seatNumbers;

    @JsonProperty("full_name")
    @NotBlank(message = "Họ tên không được để trống")
    private String passengerName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ")
    private String passengerPhone;

    @JsonProperty("email")
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String passengerEmail;

    @JsonProperty("pick_up_point")
    private String pickUpPoint;

    @JsonProperty("drop_off_point")
    private String dropOffPoint;

    @JsonProperty("require_shuttle")
    private boolean requireShuttle = false;

    // Nếu cần thêm phương thức thanh toán:
    // @JsonProperty("payment_method")
    // private String paymentMethod;
}
