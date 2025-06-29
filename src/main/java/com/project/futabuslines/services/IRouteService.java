package com.project.futabuslines.services;

import com.project.futabuslines.response.RouteResponse;
import com.project.futabuslines.dtos.RouteDTO;
import com.project.futabuslines.dtos.RouteSearchRequest;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Route;
import com.project.futabuslines.models.Trip;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRouteService {
    Route createRoute(RouteDTO routeDTO) throws Exception;
    List<Route> getAllRoute();
    List<RouteResponse> getAllRoutes();
    Route updateRoute(long routeId, RouteDTO routeDTO);
    void deleteRoute(long id);
    List<Trip> findAllTrip(RouteSearchRequest request) throws DataNotFoundException;
}
