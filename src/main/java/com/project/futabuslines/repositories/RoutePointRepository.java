package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Route;
import com.project.futabuslines.models.RoutePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutePointRepository extends JpaRepository<RoutePoint, Long> {
    List<RoutePoint> findByTripId(Long TripId);
}
