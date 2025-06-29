package com.project.futabuslines.services;

import com.project.futabuslines.dtos.BookingDTO;
import com.project.futabuslines.dtos.TripDTO;
import com.project.futabuslines.dtos.TripSearchDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.models.Ticket;
import com.project.futabuslines.models.Trip;
import com.project.futabuslines.response.TicketDetailsResponse;
import com.project.futabuslines.response.TicketResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ITicketService {
    TicketDetailsResponse createTicket(BookingDTO tripDTO, HttpServletRequest request) throws Exception;
    List<Ticket> getAllTicket();
}
