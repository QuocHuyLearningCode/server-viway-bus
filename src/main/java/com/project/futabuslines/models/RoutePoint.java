package com.project.futabuslines.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "route_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address", length = 200)
    private String address = "";

    @Column(name = "location", length = 200)
    private String location = "";

    @Column(name = "estimated_time", nullable = false)
    private LocalTime estimatedTime;

    @Column(name = "departure_point")
    private Boolean departurePoint;

    @Column(name = "arrival_point")
    private Boolean arrivalPoint;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonIgnore
    private Trip trip;
}
