package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SeatDTO {
    @JsonProperty("seat_code") // <===== Mã Ghế
    private String seatCode;

    private String status; // <===== Trạng thái

    @JsonProperty("is_active") // <===== Còn Trống = 1 / Đã đặt = 0
    private Boolean isActive;
}
