package com.depot.ims.models.compositeKeys;

import com.depot.ims.models.Item;
import com.depot.ims.models.Shipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Reference;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the primary key of Ship entity. It addresses a Many-to-Many relationship between
 * shipments and items. Therefore, every unique tuple of (itemId, shipmentId)
 */
@Data
@ToString
@AllArgsConstructor
@Builder
public class ShipKey implements Serializable {

    @Reference(to = Item.class)
    private Long itemId;

    @Reference(to = Shipment.class)
    private Long shipmentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipKey that = (ShipKey) o;
        return Objects.equals(itemId, that.itemId) &&
                Objects.equals(shipmentId, that.shipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, shipmentId);
    }
}
