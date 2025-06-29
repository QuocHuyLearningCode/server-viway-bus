package com.project.futabuslines.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin", nullable = false)
    private String origin = "";

    @Column(name = "destination", nullable = false)
    private String destination = "";

    @Column(name = "distance", nullable = false)
    private Double distance;

    @Column(name = "estimated_duration", nullable = false)
    private String estimatedDuration;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

}

