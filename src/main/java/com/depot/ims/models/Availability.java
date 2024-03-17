package com.depot.ims.models;

import com.depot.ims.models.compositeKeys.AvailabilityKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(AvailabilityKey.class)
@Table(name = "Availabilities")
public class Availability {

    @Id
    @ManyToOne
    @JoinColumn(name = "PK1_FK_availabilities_sites")
    private Site siteId;

    @Id
    @ManyToOne
    @JoinColumn(name = "PK2_FK_availabilities_items")
    private Item itemId;

    @Column(nullable = false)
    private Integer quantity;

}
