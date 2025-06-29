package com.project.futabuslines.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.Route;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteResponse {
    private Long id;
    private String origin;
    private String destination;
    private Double distance;

    @JsonProperty("estimated_duration")
    private String estimatedDuration;

    @JsonProperty("vehicle_kind")
    private String vehicleKind;

    public static RouteResponse fromRoute(Route route) {
        return RouteResponse
                .builder()
                .id(route.getId())
                .origin(route.getOrigin())
                .destination(route.getDestination())
                .distance(route.getDistance())
                .estimatedDuration(route.getEstimatedDuration())
                .vehicleKind(route.getBus().getVehicleKind())
                .build();
    }
}
