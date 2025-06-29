package com.project.futabuslines.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.RoutePoint;
import com.project.futabuslines.models.Ticket;
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
public class TicketDetailsResponse {
    private Long id;
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    private String status;

    private String route;

    @JsonProperty("total_seat")
    private int totalSeat;

    @JsonProperty("seat_code")
    private List<String> seatCode;

    @JsonIgnore
    private LocalDate pickUpTime;

    @JsonProperty("full_time")
    private String time;

    @JsonProperty("estimated_time")
    @JsonIgnore
    private LocalTime estimatedTime;

    private String pickUpPoint;

    private String dropOffPoint;

    private Long price;

    @JsonProperty("qr_code")
    private String qrCode;

    public static TicketDetailsResponse fromTicket(Ticket ticket){

        LocalDate pickupTime = LocalDate.from(ticket.getPickUpTime());
        List<RoutePoint> routePoints = ticket.getTrip().getRoutePoints();

        Optional<RoutePoint> departurePoint = routePoints.stream()
                .filter(RoutePoint::getDeparturePoint)
                .findFirst();
        LocalTime estimatedTime = departurePoint
                .map(RoutePoint::getEstimatedTime)
                .orElse(null);

        LocalTime safeEstimatedTime = estimatedTime != null ? estimatedTime : LocalTime.MIDNIGHT;

        String fullTime = LocalDateTime.of(pickupTime, safeEstimatedTime)
                .format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));

        List<String> seatCodes = Arrays.asList(ticket.getSeatCode().split(","));

        return TicketDetailsResponse.builder()
                .id(ticket.getId())
                .fullName(ticket.getFullname())
                .phoneNumber(ticket.getPhoneNumber())
                .email(ticket.getEmail())
                .status(ticket.getStatus())
                .route(ticket.getTrip().getRoute().getOrigin() + " - " + ticket.getTrip().getRoute().getDestination())
                .totalSeat(seatCodes.size())
                .seatCode(seatCodes)
                .time(fullTime)
                .pickUpPoint(ticket.getPickUpPoint())
                .dropOffPoint(ticket.getDropOffPoint())
                .price(ticket.getPrice())
                .qrCode(ticket.getQrCode())
                .build();
    }
}
