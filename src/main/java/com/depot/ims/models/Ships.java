package com.depot.ims.models;

import com.depot.ims.models.compositeKeys.ShipKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ShipKey.class)
public class Ships {

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

    @Column(nullable = false)
    private Integer quantity;
}
