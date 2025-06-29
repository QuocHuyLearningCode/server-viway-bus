package com.project.futabuslines.repositories;

import com.project.futabuslines.models.SeatTrip;
import com.project.futabuslines.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatTripRepository extends JpaRepository<SeatTrip, Long> {
    List<SeatTrip> findByTripIdAndSeatCodeIn(Long tripId, List<String> seatCodes);
    List<SeatTrip> findByTripIdAndIsBookedTrue(Long tripId);
    List<SeatTrip> findByTripId(Long tripId);
}
