package com.project.futabuslines.services;

import com.project.futabuslines.dtos.TripDTO;
import com.project.futabuslines.dtos.TripSearchDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Trip;
import com.project.futabuslines.response.TripSearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITripService {
    Trip createTrip(TripDTO tripDTO) throws Exception;
    List<Trip> getAllTrip();
    Trip updateTrip(long tripId, TripDTO tripDTO);
    void deleteTrip(long id);
    List<TripSearchResponse> tripSearch(TripSearchDTO tripSearchDTO) throws DataNotFoundException;

}
