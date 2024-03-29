package com.depot.ims.models.compositeKeys;

import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import lombok.Data;
import org.springframework.data.annotation.Reference;

import java.io.Serializable;
import java.util.Objects;

@Data
public class AvailabilityKey implements Serializable {

    @Reference(to = Site.class)
    private Long siteId;

    @Reference(to = Item.class)
    private Long itemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilityKey that = (AvailabilityKey) o;
        return Objects.equals(siteId, that.siteId) &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(siteId, itemId);
    }
}
