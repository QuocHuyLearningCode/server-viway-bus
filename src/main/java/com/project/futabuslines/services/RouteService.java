package com.project.futabuslines.services;

import com.project.futabuslines.response.RouteResponse;
import com.project.futabuslines.dtos.RouteDTO;
import com.project.futabuslines.dtos.RouteSearchRequest;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Bus;
import com.project.futabuslines.models.Route;
import com.project.futabuslines.models.Trip;
import com.project.futabuslines.repositories.BusRepository;
import com.project.futabuslines.repositories.RouteRepository;
import com.project.futabuslines.repositories.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RouteService implements IRouteService{
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final TripRepository tripRepository;
    @Override
    // TAO CHUYEN
    public Route createRoute(RouteDTO routeDTO) throws Exception{
        Bus bus = busRepository.findById(routeDTO.getBusId()).orElseThrow(()-> new DataNotFoundException("Cannot find Bus with id: " + routeDTO.getBusId()));
        Route newRoute = Route
                .builder()
                .origin(routeDTO.getOrigin())
                .destination(routeDTO.getDestination())
                .distance(routeDTO.getDistance())
                .estimatedDuration(routeDTO.getEstimatedDuration())
                .bus(bus)
                .build();
        return routeRepository.save(newRoute);
    }

    // LAY TOAN BO
    @Override
    public List<Route> getAllRoute() {
        return routeRepository.findAll();
    }

    @Override
    public List<RouteResponse> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(RouteResponse::fromRoute)
                .collect(Collectors.toList());
    }


    @Override
    public Route updateRoute(long routeId, RouteDTO routeDTO) {
        return null;
    }

    @Override
    public void deleteRoute(long id) {

    }

    @Override
    public List<Trip> findAllTrip(RouteSearchRequest request) throws DataNotFoundException {
        // Bạn cần tìm route theo thông tin được gửi
        Optional<Route> routeOpt = routeRepository.findByOriginAndDestinationAndDistanceAndEstimatedDurationAndBus_VehicleKind(
                request.getOrigin(),
                request.getDestination(),
                request.getDistance(),
                request.getEstimatedDuration(),
                request.getVehicleKind()
        );

        if (routeOpt.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy tuyến phù hợp.");
        }

        Route route = routeOpt.get();
        return tripRepository.findByRoute(route);
    }
}
