package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Availabilities {

    @Id
    @Column(name = "PK1_FK_availabilities_sites", updatable = false, nullable = false)
    private Integer id;

    @Id
    @Column(name = "PK2_FK_availabilities_items", updatable = false, nullable = false)
    private Integer itemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}
