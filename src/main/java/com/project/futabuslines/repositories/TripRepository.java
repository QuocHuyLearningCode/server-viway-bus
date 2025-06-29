package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Role;
import com.project.futabuslines.models.Route;
import com.project.futabuslines.models.Ticket;
import com.project.futabuslines.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRoute(Route route);
    List<Trip> findByRoute_OriginAndRoute_DestinationAndDepartureTimeBetweenAndAvailableSeatsGreaterThanEqual(
            String origin,
            String destination,
            LocalDate startOfDay,
            LocalDate endOfDay,
            Integer availableSeats
    );
    List<Trip> findByDepartureTime(LocalDate departureTime);
    boolean existsByRouteIdAndDepartureTimeAndTimeStartAndTimeEndAndPrice(
            Long routeId,
            LocalDate departureTime,
            LocalTime timeStart,
            LocalTime timeEnd,
            Long price);



}
