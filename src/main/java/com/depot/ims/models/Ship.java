package com.depot.ims.models;

import com.depot.ims.models.keys.ShipKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a ship entity. Encapsulates user details and credentials for access control and
 * identification. Annotations from JPA are used for ORM (Object Relational Mapping) to a database
 * table, while Lombok annotations reduce boilerplate code for standard Java functionalities.
 */
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

  /**
   * Override toString() so that only the entire Item and Shipment within this ship entity won't be
   * printed out. Instead, after applying toString(), a ship entity will only contain the simplified
   * format: "shipmentId: ??, itemId: ??, quantity: ??"
   *
   * @return the simplified string representation of a ship entity
   */
  @Override
  public String toString() {
    return "shipmentId: "
        + shipmentId.getShipmentId()
        + ", itemId: "
        + itemId.getItemId()
        + " ,quantity: "
        + quantity;
  }
}
