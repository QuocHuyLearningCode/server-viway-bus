package com.project.futabuslines.services;

import com.project.futabuslines.dtos.RouteSearchRequest;
import com.project.futabuslines.dtos.TripDTO;
import com.project.futabuslines.dtos.TripSearchDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.*;
import com.project.futabuslines.repositories.*;
import com.project.futabuslines.response.TripSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TripService implements ITripService{
    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final SeatTripRepository seatTripRepository;
    private final SeatRepository seatRepository;

    @Override
    public Trip createTrip(TripDTO tripDTO) throws Exception {
        Route route = routeRepository
                .findById(tripDTO.getRouteId()).orElseThrow(
                        ()-> new DataNotFoundException("Cannot find Bus with id: " + tripDTO.getRouteId()));
        Trip newTrip = Trip.builder()
                .route(route)
                .departureTime(tripDTO.getDepartureTime())
                .timeStart(tripDTO.getTimeStart())
                .timeEnd(tripDTO.getTimeEnd())
                .price(tripDTO.getPrice())
                .availableSeats(tripDTO.getAvailableSeats())
                .isActive(true)
                .build();
        return tripRepository.save(newTrip);
    }

    @Override
    public List<Trip> getAllTrip() {
        return tripRepository.findAll();
    }

    @Override
    public Trip updateTrip(long tripId, TripDTO tripDTO) {
        return null;
    }

    @Override
    public void deleteTrip(long id) {
        tripRepository.deleteById(id);
    }

    @Override
    public List<TripSearchResponse> tripSearch(TripSearchDTO tripSearchDTO) throws DataNotFoundException {
        LocalDate departureDate = tripSearchDTO.getDepartureDate();
        LocalDate startOfDay = departureDate; // không cần gọi atStartOfDay()
        LocalDate endOfDay = departureDate;

        List<Trip> trips = tripRepository.findByRoute_OriginAndRoute_DestinationAndDepartureTimeBetweenAndAvailableSeatsGreaterThanEqual(
                tripSearchDTO.getOrigin(),
                tripSearchDTO.getDestination(),
                startOfDay,
                endOfDay,
                tripSearchDTO.getPassengers()
        );
        List<TripSearchResponse> tripResponses = trips.stream()
                .map(trip -> {
                    Long busId = trip.getRoute().getBus().getId();
                    List<Seat> seats = seatRepository.findByBusId(busId);
                    return TripSearchResponse.fromTripSearh(trip, seats);
                })
                .collect(Collectors.toList());

        if (trips.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy chuyến xe phù hợp");
        }

        return tripResponses;
    }

    public List<String> getBookedSeats(Long tripId) {
        List<SeatTrip> seatTrips = seatTripRepository.findByTripId(tripId);

        return seatTrips.stream()
                .filter(seat ->
                        Boolean.TRUE.equals(seat.getIsBooked()) ||
                                TicketStatus.PENDING.equals(seat.getStatus())
                )
                .map(SeatTrip::getSeatCode)
                .collect(Collectors.toList());
    }


    @Scheduled(cron = "*/10 * * * * *")// Chạy lúc 00:00 mỗi ngày
    public void deactivateOldOrSoldOutTrips() {
        LocalDate today = LocalDate.now();

        List<Trip> trips = tripRepository.findAll();
        for (Trip trip : trips) {
            boolean isPast = trip.getDepartureTime().isBefore(today);
            boolean isFull = trip.getAvailableSeats() != null && trip.getAvailableSeats() <= 0;
            if (isPast || isFull) {
                trip.setIsActive(false);
            }
        }
        tripRepository.saveAll(trips);
    }

    public void updateTripStatus() {
        LocalDate today = LocalDate.now();

        List<Trip> trips = tripRepository.findAll();
        for (Trip trip : trips) {
            boolean isPastDate = trip.getDepartureTime().isBefore(today);
            boolean isFull = trip.getAvailableSeats() != null && trip.getAvailableSeats() <= 0;

            if ((isPastDate || isFull) && trip.getIsActive()) {
                trip.setIsActive(false);
            }
        }

        tripRepository.saveAll(trips);
    }

    public int generateTripsFromTemplate(LocalDate templateDate, int daysToGenerate) {
        List<Trip> templateTrips = tripRepository.findByDepartureTime(templateDate);

        if (templateTrips.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy chuyến đi mẫu vào ngày: " + templateDate);
        }

        List<Trip> newTrips = new ArrayList<>();

        for (int i = 1; i <= daysToGenerate; i++) {
            LocalDate newDate = templateDate.plusDays(i);

            for (Trip template : templateTrips) {

                boolean exists = tripRepository.existsByRouteIdAndDepartureTimeAndTimeStartAndTimeEndAndPrice(
                        template.getRoute().getId(),
                        newDate,
                        template.getTimeStart(),
                        template.getTimeEnd(),
                        template.getPrice()
                );

                if (exists) {
                    System.out.println("⚠️ Đã tồn tại chuyến: " + template.getRoute().getId() + " ngày " + newDate);
                    continue; // hoặc delete và tạo lại
                }

                Trip newTrip = Trip.builder()
                        .route(template.getRoute())
                        .departureTime(newDate)
                        .timeStart(template.getTimeStart())
                        .timeEnd(template.getTimeEnd())
                        .price(template.getPrice())
                        .availableSeats(template.getAvailableSeats())
                        .isActive(true)
                        .build();

                newTrips.add(newTrip);
            }
        }

        tripRepository.saveAll(newTrips);
        return newTrips.size();
    }

}

