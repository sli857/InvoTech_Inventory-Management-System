package com.depot.ims.models;

import com.depot.ims.models.keys.AvailabilityKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents an availability entity in the system. Encapsulates user details and credentials for
 * access control and identification. Annotations from JPA are used for ORM (Object Relational
 * Mapping) to a database table, while Lombok annotations reduce boilerplate code for standard Java
 * functionalities.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods.
@NoArgsConstructor // Generates a no-argument constructor.
@AllArgsConstructor // Generates a constructor initializing all fields.
@ToString
@Builder
@Entity // Indicates that this class is a JPA entity.
@IdClass(AvailabilityKey.class)
@Table(name = "Availabilities") // Maps this entity to the "Availabilities" table in the database.
public class Availability {

  @Id // Marks this field as the foreign key.
  @ManyToOne
  @JoinColumn(name = "PK1_FK_availabilities_sites")
  private Site siteId;

  @Id // Marks this field as the foreign key.
  @ManyToOne
  @JoinColumn(name = "PK2_FK_availabilities_items")
  private Item itemId;

  // Specifies the quantity column, indicating the quantity of the item in the site
  @Column(nullable = false)
  private Integer quantity;
}
