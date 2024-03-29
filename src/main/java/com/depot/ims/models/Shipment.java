package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Builder
@Table(name = "Shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_shipments", updatable = false, nullable = false)
    private Long shipmentId;

    @Column(name = "FK_source", nullable = false)
    private Integer source;

    @Column(name = "FK_destination", nullable = false)
    private Integer destination;

    @Column(name = "current_location")
    private String currentLocation;

    @Column(name = "departure_time")
    private Timestamp departureTime;

    @Column(name = "estimated_arrival_time")
    private Timestamp estimatedArrivalTime;

    @Column(name = "actual_arrival_time")
    private Timestamp actualArrivalTime;

    @Column(name = "shipment_status", nullable = false)
    private String shipmentStatus;

}
