package com.project.futabuslines.services;

import com.project.futabuslines.response.RouteResponse;
import com.project.futabuslines.dtos.RouteDTO;
import com.project.futabuslines.dtos.RoutePointDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.RoutePoint;
import com.project.futabuslines.models.Trip;
import com.project.futabuslines.repositories.RoutePointRepository;
import com.project.futabuslines.repositories.RouteRepository;
import com.project.futabuslines.repositories.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoutePointService implements IRoutePointService{
    private final RoutePointRepository routePointRepository;
    private final RouteRepository routeRepository;
    private final TripRepository tripRepository;
    @Override
    public RoutePoint createRoutePoint(RoutePointDTO routePointDTO) throws Exception {
        Trip trip = tripRepository.findById(routePointDTO.getTripId()).orElseThrow(()-> new DataNotFoundException("Cannot find Bus with id: " + routePointDTO.getTripId()));
        RoutePoint newRoute = RoutePoint.builder()
                .trip(trip)
                .departurePoint(routePointDTO.getDeparturePoint())
                .arrivalPoint(routePointDTO.getArrivalPoint())
                .address(routePointDTO.getAddress())
                .location(routePointDTO.getLocation())
                .estimatedTime(routePointDTO.getEstimatedTime())
                .build();
        return routePointRepository.save(newRoute);
    }

    @Override
    public List<RoutePoint> getAllRoutePoint() {
        return routePointRepository.findAll();
    }

    @Override
    public List<RouteResponse> getAllRoutesPoint() {
        return List.of();
    }

    @Override
    public RoutePoint updateRoutePoint(long routeId, RouteDTO routeDTO) {
        return null;
    }

    @Override
    public void deleteRoutePoint(long id) {

    }

    @Override
    public List<RoutePoint> findByRoute(Long id) {
        return routePointRepository.findByTripId(id);
    }
}
