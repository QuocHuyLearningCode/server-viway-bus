package com.project.futabuslines.controllers;

import com.project.futabuslines.dtos.BusDTO;
import com.project.futabuslines.models.Bus;
import com.project.futabuslines.services.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/bus")
@RequiredArgsConstructor
public class BusController {
    private final BusService busService;

    // POST: http://localhost:8080/api/v1/bus/create-bus
    @PostMapping("/create-bus")
    public ResponseEntity<?> createBus(
            @Valid @RequestBody BusDTO busDTO,
            BindingResult result
    ){
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        busService.createBus(busDTO);
        return ResponseEntity.ok("Insert Bus successfully");
    }

    // GET: http://localhost:8080/api/v1/bus/get-all-bus
    @GetMapping("/get-all-bus")
    public ResponseEntity<List<Bus>> getAllBus(){
        List<Bus> bus = busService.getAllBus();
        return ResponseEntity.ok(bus);
    }

    @GetMapping("/get-bus/{id}")
    public ResponseEntity<?> getBusById(@PathVariable Long id){
       Bus bus = busService.getBusById(id);
       return ResponseEntity.ok(bus);
    }

    // PUT: http://localhost:8088/api/v1/bus/update-bus/{id}
    @PutMapping("/update-bus/{id}")
    public ResponseEntity<String> updateBus(
            @PathVariable Long id,
            @Valid @RequestBody BusDTO busDTO
    ){
        busService.updateBus(id, busDTO);
        return ResponseEntity.ok("Update Bus successfully" );
    }

    // DELETE: http://localhost:8088/api/v1/categories/delete-bus/{id}
    @DeleteMapping("/delete-bus/{id}")
    public ResponseEntity<String> deleteCategory( @PathVariable Long id){
        busService.deleteBus(id);
        return ResponseEntity.ok("Delete Bus with id = " + id);
    }
}

