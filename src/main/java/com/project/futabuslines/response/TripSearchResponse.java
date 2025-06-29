package com.project.futabuslines.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.RoutePoint;
import com.project.futabuslines.models.Seat;
import com.project.futabuslines.models.Trip;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class TripSearchResponse {
    private Long id;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate departureTime;

    @JsonProperty("time_start")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeStart;

    @JsonProperty("time_end")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeEnd;

    private Long price;

    @JsonProperty("available_seats")
    private Integer availableSeats;

    private Double distance;
    @JsonProperty("estimated_duration")
    private String estimatedDuration;

    @JsonProperty("vehicle_kind")
    private String vehicleKind;

    @JsonProperty("start_location")
    private String startLocation;

    @JsonProperty("end_location")
    private String endLocation;

    private List<Seat> seats;
    public static TripSearchResponse fromTripSearh(Trip trip, List<Seat> seats){
        String startLocation = null;
        String endLocation = null;
        for (RoutePoint rp : trip.getRoutePoints()) {
            if (rp.getDeparturePoint() != null && rp.getDeparturePoint()) {
                startLocation = rp.getLocation();
            }
            if (rp.getArrivalPoint() != null && rp.getArrivalPoint()) {
                endLocation = rp.getLocation();
            }
        }
        return TripSearchResponse.builder()
                .id(trip.getId())
                .departureTime(trip.getDepartureTime())
                .timeStart(trip.getTimeStart())
                .timeEnd(trip.getTimeEnd())
                .price(trip.getPrice())
                .availableSeats(trip.getAvailableSeats())
                .distance(trip.getRoute().getDistance())
                .estimatedDuration(trip.getRoute().getEstimatedDuration())
                .vehicleKind(trip.getRoute().getBus().getVehicleKind())
                .startLocation(startLocation)
                .endLocation(endLocation)
                .seats(seats)
                .build();
    }

}
