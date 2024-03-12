package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "PK_items", updatable = false, nullable = false)
    private Integer itemId;

    @Column(name = "item_name", unique = true, nullable = false)
    private String itemName;

    @Column(name = "item_price", nullable = false)
    private Double itemPrice;
}
