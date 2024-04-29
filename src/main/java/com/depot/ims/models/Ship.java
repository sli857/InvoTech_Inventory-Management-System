package com.depot.ims.models;

import com.depot.ims.models.compositeKeys.ShipKey;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a ship entity.
 * Encapsulates user details and credentials for access control and identification.
 * Annotations from JPA are used for ORM (Object Relational Mapping) to a database table,
 * while Lombok annotations reduce boilerplate code for standard Java functionalities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
@IdClass(ShipKey.class)
@Table(name = "Ships")
public class Ship {

    @Id
    @ManyToOne
    // @JoinColumn(name = "PK1_FK_ships_items", referencedColumnName = "PK_items")
    @JoinColumn(name = "PK1_FK_ships_items")
    private Item itemId;

    @Id
    @ManyToOne
    // @JoinColumn(name = "PK2_FK_ships_shipments", referencedColumnName = "PK_shipments")
    @JoinColumn(name = "PK2_FK_ships_shipments")
    private Shipment shipmentId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
