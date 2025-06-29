package com.project.futabuslines.services;

import com.project.futabuslines.dtos.BusDTO;
import com.project.futabuslines.models.Bus;
import com.project.futabuslines.models.Seat;
import com.project.futabuslines.repositories.BusRepository;
import com.project.futabuslines.repositories.SeatRepository;
import com.project.futabuslines.response.BusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class BusService implements IBusService{
    private final BusRepository busRepository;
    private final SeatRepository seatRepository;

    @Override
    public Bus createBus(BusDTO busDTO) {
        Bus newBus = Bus
                .builder()
                .operator(busDTO.getOperator())
                .vehicleKind(busDTO.getVehicleKind())
                .totalSeats(busDTO.getTotalSeats())
                .build();
        return busRepository.save(newBus);
    }

    @Override
    public Bus getBusById(long id) {
        return busRepository.findById(id)
                .orElseThrow(()->new RuntimeException("ID xe không đúng hoặc xe không tồn tại."));
    }

    public BusResponse getSeatCodesByBusId(Long busId) {
        List<Seat> seats = seatRepository.findByBusId(busId);
        List<String> seatCodes = seats.stream()
                .map(Seat::getSeatCode)
                .collect(Collectors.toList());

        return BusResponse.builder()
                .seatCodes(seatCodes)
                .build();
    }

    @Override
    public List<Bus> getAllBus() {
        return busRepository.findAll();
    }

    @Override
    public Bus updateBus(long busId, BusDTO busDTO) {
        Bus existingBus = getBusById(busId);
        existingBus.setOperator(busDTO.getOperator());
        existingBus.setVehicleKind(busDTO.getVehicleKind());
        existingBus.setTotalSeats(busDTO.getTotalSeats());
        return busRepository.save(existingBus);
    }

    @Override
    public void deleteBus(long id) {
        busRepository.deleteById(id);
    }
}
