package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.RouteDTO;
import com.project.futabuslines.dtos.RoutePointDTO;
import com.project.futabuslines.models.RoutePoint;
import com.project.futabuslines.services.RoutePointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("${api.prefix}/route-point")
@RequiredArgsConstructor
public class RoutePointController {
    private final RoutePointService routePointService;

    // POST: http://localhost:8080/api/v1/route-point
    @PostMapping("")
    public ResponseEntity<?> createRoutePoint(
            @Valid @RequestBody RoutePointDTO routeDTO,
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
            routePointService.createRoutePoint(routeDTO);
            return ResponseEntity.ok("Insert Bus successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

//    // GET: http://localhost:8080/api/v1/bus
//    @GetMapping("/all-routes")
//    public ResponseEntity<List<RouteResponse>> getAllRoutes(){
//        List<RouteResponse> route = routeService.getAllRoutes();
//        return ResponseEntity.ok(route);
//    }

    // GET: http://localhost:8080/api/v1/route-point
    @GetMapping("")
    public ResponseEntity<List<RoutePoint>> getAllRoute(){
        List<RoutePoint> route = routePointService.getAllRoutePoint();
        return ResponseEntity.ok(route);
    }

    // PUT: http://localhost:8080/api/v1/route-point/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody RouteDTO routeDTO
    ){
        routePointService.updateRoutePoint(id, routeDTO);
        return ResponseEntity.ok("Update Route Point successfully" );
    }

    // DELETE: http://localhost:8088/api/v1/route-point/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoute(
            @PathVariable Long id
    ){
        routePointService.deleteRoutePoint(id);
        return  ResponseEntity.ok("Delete Bus with id = " + id);
    }

    // GET: http://localhost:8088/api/v1/route-point/trip/{tripID}
    @GetMapping("/trip/{tripId}")
    // Danh sách order details của một order nào đó
    public ResponseEntity<?> getRouteByTrip(
            @Valid @PathVariable("tripId") Long tripId
    ){
        List<RoutePoint> orderDetails = routePointService.findByRoute(tripId);
//        List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(orderId);
        return ResponseEntity.ok(orderDetails);
    }

}
