package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data // => have toString();
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteSearchRequest {
    private String origin;

    private String destination;

    private Double distance;

    @JsonProperty("estimated_duration")
    private String estimatedDuration;

    @JsonProperty("vehicle_kind")
    private String vehicleKind;
}
