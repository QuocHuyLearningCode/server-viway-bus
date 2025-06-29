package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutePointDTO {
    @JsonProperty("estimated_time")
    private LocalTime estimatedTime; // <===== Thời gian - Ví dụ: 9:00

    private String location; // <===== Vị trí - Ví dụ: BX Miền Đông Mới

    private String address; // <===== Địa chỉ cụ thể - Ví dụ: 39448 Xa Lộ Hà Nội, Phường Long Bình, TP Thủ Đức, TPHCM

    @JsonProperty("departure_point")
    private Boolean departurePoint;

    @JsonProperty("arrival_point")
    private Boolean arrivalPoint;

    @JsonProperty("trip_id")
    private Long tripId;
}
