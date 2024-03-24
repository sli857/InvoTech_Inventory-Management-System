package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_items", updatable = false, nullable = false)
    private Long itemId;

    @Column(name = "item_name", unique = true, nullable = false)
    private String itemName;

    @Column(name = "item_price", nullable = false)
    private Double itemPrice;

    @OneToMany(mappedBy = "itemId", cascade = CascadeType.ALL)
    private List<Availability> availabilities;

    @OneToMany(mappedBy = "itemId", cascade = CascadeType.ALL)
    private List<Ship> ships;
}
