package com.depot.ims.models;

import com.depot.ims.models.compositeKeys.ShipKey;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Override
    public String toString(){
        return "shipmentId: " + shipmentId.getShipmentId() + ", itemId: " + itemId.getItemId() +
                " ,quantity: " + quantity;
    }
}
