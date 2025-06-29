package com.project.futabuslines.services;

import com.project.futabuslines.response.RouteResponse;
import com.project.futabuslines.dtos.RouteDTO;
import com.project.futabuslines.dtos.RoutePointDTO;
import com.project.futabuslines.models.RoutePoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRoutePointService {
    RoutePoint createRoutePoint(RoutePointDTO routePointDTO) throws Exception;
    List<RoutePoint> getAllRoutePoint();
    List<RouteResponse> getAllRoutesPoint();
    RoutePoint updateRoutePoint(long routeId, RouteDTO routeDTO);
    void deleteRoutePoint(long id);
    List<RoutePoint> findByRoute(Long id);
}
