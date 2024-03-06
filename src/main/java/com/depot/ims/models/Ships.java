package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Ships {

    @Id
    @Column(name = "PK1_FK_ships_items", updatable = false, nullable = false)
    private Integer id;

    @Id
    @Column(name = "PK2_FK_ships_shipments", updatable = false, nullable = false)
    private Integer shipmentId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
