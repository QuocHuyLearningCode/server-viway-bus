package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusDTO {

    private String operator = "Nhà xe Phương Trang";

    @JsonProperty("vehicle_kind")
    @NotEmpty(message = "Tên loại không thể bỏ trống")
    private String vehicleKind;


    @JsonProperty("total_seats")
    @Min(value = 1, message = "Total seat must be >0")
    private Integer totalSeats;
}
