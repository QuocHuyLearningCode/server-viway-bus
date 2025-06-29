package com.project.futabuslines.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "buses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operator", length = 200)
    private String operator = "";

    @Column(name = "vehicle_kind", length = 200)
    private String vehicleKind = "";

    @Column(name = "total_seats")
    private Integer totalSeats;

    @OneToMany(mappedBy = "bus")
    private List<Seat> seats;
}
