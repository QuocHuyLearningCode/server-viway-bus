package com.project.futabuslines.controllers;

import com.project.futabuslines.response.RouteResponse;
import com.project.futabuslines.dtos.RouteDTO;
import com.project.futabuslines.dtos.RouteSearchRequest;
import com.project.futabuslines.models.Route;
import com.project.futabuslines.models.Trip;
import com.project.futabuslines.services.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("${api.prefix}/route")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    // POST: http://localhost:8080/api/v1/route
    @PostMapping("")
    public ResponseEntity<?> createRoute(
            @Valid @RequestBody RouteDTO routeDTO,
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
            routeService.createRoute(routeDTO);
            return ResponseEntity.ok("Insert Bus successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // GET: http://localhost:8080/api/v1/route/all-routes
    @GetMapping("/all-routes")
    public ResponseEntity<List<RouteResponse>> getAllRoutes(){
        List<RouteResponse> route = routeService.getAllRoutes();
        return ResponseEntity.ok(route);
    }

    // GET: http://localhost:8080/api/v1/route
    @GetMapping("")
    public ResponseEntity<List<Route>> getAllRoute(){
        List<Route> route = routeService.getAllRoute();
        return ResponseEntity.ok(route);
    }

    // PUT: http://localhost:8088/api/v1/route/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody RouteDTO routeDTO
    ){
        routeService.updateRoute(id, routeDTO);
        return ResponseEntity.ok("Update Bus successfully" );
    }

    // DELETE: http://localhost:8088/api/v1/route/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoute(
            @PathVariable Long id
    ){
        routeService.deleteRoute(id);
        return  ResponseEntity.ok("Delete Bus with id = " + id);
    }

    // // POST: http://localhost:8080/api/v1/route/search-trips
    @PostMapping("/search-trips")
    public ResponseEntity<?> searchTrips(@RequestBody RouteSearchRequest request) {
        try {
            List<Trip> trips = routeService.findAllTrip(request);
            return ResponseEntity.ok(trips); // hoặc map sang TripResponse nếu muốn
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
