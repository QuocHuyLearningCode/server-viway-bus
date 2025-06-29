package com.project.futabuslines.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trip_seat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_code", length = 200)
    private String seatCode;

    @Column(name = "status")
    private String status;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
}
