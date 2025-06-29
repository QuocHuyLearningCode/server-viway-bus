package com.project.futabuslines.services;

import com.project.futabuslines.dtos.BusDTO;
import com.project.futabuslines.models.Bus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBusService {
    // ===== ADMIN  =====
    Bus createBus(BusDTO busDTO);
    Bus getBusById(long id);
    List<Bus> getAllBus();
    Bus updateBus(long busId, BusDTO busDTO);
    void deleteBus(long id);
}
