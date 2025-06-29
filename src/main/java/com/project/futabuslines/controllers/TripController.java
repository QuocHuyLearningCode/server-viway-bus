package com.project.futabuslines.controllers;
import com.project.futabuslines.dtos.RoutePointDTO;
import com.project.futabuslines.dtos.TripDTO;
import com.project.futabuslines.dtos.TripSearchDTO;
import com.project.futabuslines.models.SeatTrip;
import com.project.futabuslines.models.Ticket;
import com.project.futabuslines.models.TicketStatus;
import com.project.futabuslines.models.Trip;
import com.project.futabuslines.repositories.SeatTripRepository;
import com.project.futabuslines.response.BusResponse;
import com.project.futabuslines.response.TripSearchResponse;
import com.project.futabuslines.services.BusService;
import com.project.futabuslines.services.RoutePointService;
import com.project.futabuslines.services.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/trip")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;
    private final SeatTripRepository seatTripRepository;
    private final BusService busService;

    // POST: http://localhost:8080/api/v1/trip
    @PostMapping("")
    public ResponseEntity<?> createRoutePoint(
            @Valid @RequestBody TripDTO tripDTO,
            BindingResult result
    ){
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            tripService.createTrip(tripDTO);
            tripService.updateTripStatus();
            return ResponseEntity.ok("Insert Bus successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET: http://localhost:8080/api/v1/trip/seats/{busId}
    @GetMapping("/seats/{busId}")
    public ResponseEntity<BusResponse> getSeatsByBusId(@PathVariable Long busId) {
        BusResponse response = busService.getSeatCodesByBusId(busId);
        return ResponseEntity.ok(response);
    }

    // GET: http://localhost:8080/api/v1/trip
    @GetMapping("")
    public ResponseEntity<List<Trip>> getAllTrip(){
        List<Trip> trip = tripService.getAllTrip();
        return ResponseEntity.ok(trip);
    }

    // POST: http://localhost:8080/api/v1/trip/search-trips
    @PostMapping("/search-trips")
    public ResponseEntity<?> searchTrips(@RequestBody TripSearchDTO tripSearchDTO) {
        try {
            List<TripSearchResponse> trips = tripService.tripSearch(tripSearchDTO);
            return ResponseEntity.ok(trips); // hoặc map sang TripResponse nếu muốn
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE: http://localhost:8080/api/v1/trip/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTrip( @PathVariable Long id){
        tripService.deleteTrip(id);
        return ResponseEntity.ok("Delete Bus with id = " + id);
    }

    // GET: http://localhost:8080/api/v1/trip/seat-trip/{tripId}
    @GetMapping("/seat-trip/{tripId}")
    public ResponseEntity<List<String>> getBookedOrPendingSeats(@PathVariable Long tripId) {
        List<SeatTrip> seatTrips = seatTripRepository.findByTripId(tripId);
        List<String> seatCodes = seatTrips.stream()
                .filter(seat ->
                        Boolean.TRUE.equals(seat.getIsBooked()) ||
                                TicketStatus.PENDING.equals(seat.getStatus())
                )
                .map(SeatTrip::getSeatCode)
                .collect(Collectors.toList());
        return ResponseEntity.ok(seatCodes);
    }

    // POST: http://localhost:8080/api/v1/trip/seat-trip/generate
    @PostMapping("/generate")
    public ResponseEntity<String> generateTrips(
            @RequestParam("templateDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate templateDate,
            @RequestParam(value = "days", defaultValue = "7") int daysToGenerate) {

        int created = tripService.generateTripsFromTemplate(templateDate, daysToGenerate);
        return ResponseEntity.ok("✅ Đã tạo " + created + " chuyến đi mới từ ngày " + templateDate);
    }
}
