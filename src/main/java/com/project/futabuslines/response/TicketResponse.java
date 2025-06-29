package com.project.futabuslines.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.RoutePoint;
import com.project.futabuslines.models.Ticket;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class TicketResponse {
    @JsonProperty("code_ticket")
    private String codeTicket;

    private String status;

    private String origin;

    private String destination;

    @JsonProperty("seat_code")
    private List<String> seatCode;

    @JsonProperty("estimated_time")
    @JsonIgnore
    private LocalTime estimatedTime;

    @JsonProperty("departure_time")
    @JsonIgnore
    private LocalDate departureTime;

    @JsonProperty("full_time")
    private String time;


    public static TicketResponse fromTicket(Ticket ticket){
        LocalDate departureTime = ticket.getTrip().getDepartureTime();
        List<RoutePoint> routePoints = ticket.getTrip().getRoutePoints();

        Optional<RoutePoint> departurePoint = routePoints.stream()
                .filter(RoutePoint::getDeparturePoint)
                .findFirst();
        LocalTime estimatedTime = departurePoint
                .map(RoutePoint::getEstimatedTime)
                .orElse(null);

        LocalTime safeEstimatedTime = estimatedTime != null ? estimatedTime : LocalTime.MIDNIGHT;

        String fullTime = LocalDateTime.of(departureTime, safeEstimatedTime)
                .format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));

        return TicketResponse.builder()
                .codeTicket(ticket.getCodeTicket())
                .status(ticket.getStatus())
                .origin(ticket.getTrip().getRoute().getOrigin())
                .destination(ticket.getTrip().getRoute().getDestination())
                .time(fullTime)
                .seatCode(Arrays.asList(ticket.getSeatCode().split(",")))
                .build();

    }

}
