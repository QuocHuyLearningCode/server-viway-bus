package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteDTO {
    @NotBlank(message = "Điểm đi không được để trống") // <===== Điểm đi
    private String origin;

    @NotBlank(message = "Điểm đến không được để trống") // <===== Điểm đến
    private String destination;

    private Double distance;

    @JsonProperty("estimated_duration")
    @NotBlank(message = "Thời gian di chuyển không được để trống")
    private String estimatedDuration;

    @JsonProperty("bus_id")
    private Long busId;
}
