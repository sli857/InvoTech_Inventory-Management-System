package com.depot.ims.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shipments {

    @Id
    @GeneratedValue
    @Column(name = "PK_shipments", updatable = false, nullable = false)
    private Integer id;

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
