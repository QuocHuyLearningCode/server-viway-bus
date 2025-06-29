package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByOriginAndDestinationAndDistanceAndEstimatedDurationAndBus_VehicleKind(
            String origin,
            String destination,
            Double distance,
            String estimatedDuration,
            String vehicleKind
    );

}
