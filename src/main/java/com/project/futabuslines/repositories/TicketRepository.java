package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Route;
import com.project.futabuslines.models.Ticket;
import com.project.futabuslines.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByCodeTicket(String codeTicket);
    Optional<Ticket> findByCodeTicket(String codeTicket);
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByPickUpTimeBetween(LocalDateTime from, LocalDateTime to);
    List<Ticket> findAllByStatusAndCreatedAtBefore(String status, LocalDateTime time);
    List<Ticket> findByTrip_DepartureTime(LocalDate departureTime);

}
