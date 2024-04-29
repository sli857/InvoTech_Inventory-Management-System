package com.depot.ims.models.compositeKeys;

import com.depot.ims.models.Item;
import com.depot.ims.models.Site;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Reference;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the primary key of Availability entity. It addresses a Many-to-Many relationship
 * between sites and items. Therefore, every unique tuple of (siteId, itemId) identifies a
 * unique Availability entity
 */
@Data
@ToString
public class AvailabilityKey implements Serializable {

    @Reference(to = Site.class)
    private Long siteId;

    @Reference(to = Item.class)
    private Long itemId;

    /**
     * override equals() for identifying whether two availability entities are identical, which
     * means this.siteId == other.siteId && this.itemId == other.itemId
     *
     * @param o the other Object to compare to
     * @return true if two Availability entities are considered identical, false or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilityKey that = (AvailabilityKey) o;
        return Objects.equals(siteId, that.siteId) &&
                Objects.equals(itemId, that.itemId);
    }
    /**
     * override hashCode() that is one-to-one mapped by each unique Availability entity
     * @return calculated hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(siteId, itemId);
    }
}
