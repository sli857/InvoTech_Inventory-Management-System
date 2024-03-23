package com.depot.ims.models.tables.compositeKeys;

import com.depot.ims.models.tables.Item;
import com.depot.ims.models.tables.Shipment;
import lombok.Data;
import org.springframework.data.annotation.Reference;

import java.io.Serializable;
import java.util.Objects;

@Data
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
