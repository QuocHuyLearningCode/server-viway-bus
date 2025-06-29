package com.project.futabuslines.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.Route;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripDTO {
    @JsonProperty("departure_time")
    private LocalDate departureTime;

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("time_start")
    private LocalTime timeStart;

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("time_end")
    private LocalTime timeEnd;

    @JsonProperty("price")
    private Long price;

    @JsonProperty("available_seats")
    private Integer availableSeats;

    @JsonProperty("route_id")
    private Long routeId;

}
