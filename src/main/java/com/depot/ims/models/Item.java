package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a item entity in the system.
 * Encapsulates user details and credentials for access control and identification.
 * Annotations from JPA are used for ORM (Object Relational Mapping) to a database table,
 * while Lombok annotations reduce boilerplate code for standard Java functionalities.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods.
@NoArgsConstructor  // Generates a no-argument constructor.
@AllArgsConstructor // Generates a constructor initializing all fields.
@Entity // Indicates that this class is a JPA entity.
@Builder
@Table(name = "Items") // Maps this entity to the "Items" table in the database.
public class Item {

    @Id // Marks this field as the primary key.
    // Configures auto-increment behavior for the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // primary key
    @Column(name = "PK_items", updatable = false, nullable = false)
    private Long itemId;

    // Specifies the itemName column
    @Column(name = "item_name", nullable = false)
    private String itemName;

    // Specifies the itemPrice column
    @Column(name = "item_price", nullable = false)
    private Double itemPrice;

    /**
     * Constructor for Item.
     *
     * @param itemPrice Item price
     * @param itemName Item name
     */
    public Item(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

}
