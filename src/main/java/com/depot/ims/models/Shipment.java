package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

/**
 * Represents a shipment entity in the inventory management system.
 * Includes details such as source, destination, current location, and timestamps for departure and arrival.
 * Utilizes Lombok annotations for boilerplate code reduction.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods.
@NoArgsConstructor // Generates a no-argument constructor.
@AllArgsConstructor // Generates an all-argument constructor.
@Entity // Specifies that this class is an entity and is mapped to a database table.
@Builder // Provides a builder pattern for object creation.
@Table(name = "Shipments")
public class Shipment {

    @Id // Marks the field as a primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_shipments", updatable = false, nullable = false)
    private Long shipmentId;

    // Foreign key to source location.
    @Column(name = "FK_source", nullable = false)
    private Long source;

    // Foreign key to destination location.
    @Column(name = "FK_destination", nullable = false)
    private Long destination;

    // Current location of the shipment, may be null if not applicable.
    @Column(name = "current_location")
    private String currentLocation;

    // Timestamp of when the shipment departs from the source.
    @Column(name = "departure_time")
    private Timestamp departureTime;


    // Estimated timestamp of when the shipment is expected to arrive at the destination.
    @Column(name = "estimated_arrival_time")
    private Timestamp estimatedArrivalTime;

    // Actual timestamp of when the shipment arrives at the destination. May be null if not arrived.
    @Column(name = "actual_arrival_time")
    private Timestamp actualArrivalTime;

    // Current status of the shipment (e.g., In Transit, Delivered).
    @Column(name = "shipment_status", nullable = false)
    private String shipmentStatus;

}
