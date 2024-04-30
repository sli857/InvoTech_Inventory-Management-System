package com.depot.ims.models.keys;

import com.depot.ims.models.Item;
import com.depot.ims.models.Shipment;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Reference;

/**
 * Represents the primary key of Ship entity. It addresses a Many-to-Many relationship between
 * shipments and items. Therefore, every unique tuple of (itemId, shipmentId) identifies a unique
 * ship entity.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipKey implements Serializable {

  @Reference(to = Item.class)
  private Long itemId;

  @Reference(to = Shipment.class)
  private Long shipmentId;

  /**
   * Override equals() for identifying whether two ship entities are identical, which means
   * this.itemId == other.itemId && this.shipmentId == other.shipmentId.
   *
   * @param o the other Object to compare to
   * @return true if two ship entities are considered identical, false or not
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipKey that = (ShipKey) o;
    return Objects.equals(itemId, that.itemId) && Objects.equals(shipmentId, that.shipmentId);
  }

  /**
   * Override hashCode() that is one-to-one mapped by each unique Ship entity.
   *
   * @return calculated hashCode
   */
  @Override
  public int hashCode() {
    return Objects.hash(itemId, shipmentId);
  }
}
