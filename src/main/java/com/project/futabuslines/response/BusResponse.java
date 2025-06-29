package com.project.futabuslines.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.futabuslines.models.Bus;
import com.project.futabuslines.models.Seat;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BusResponse {
    @JsonProperty("seat_codes")
    private List<String> seatCodes;

    public static BusResponse fromBus(Bus bus){
        List<String> seatCodes = bus.getSeats().stream()
                .map(Seat::getSeatCode)
                .toList();

        return BusResponse.builder()
                .seatCodes(seatCodes)
                .build();
    }
}

