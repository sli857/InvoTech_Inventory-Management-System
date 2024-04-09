package com.depot.ims.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user entity in the system.
 * Encapsulates user details and credentials for access control and identification.
 * Annotations from JPA are used for ORM (Object Relational Mapping) to a database table,
 * while Lombok annotations reduce boilerplate code for standard Java functionalities.
 */
@Data // Lombok annotation to generate getters, setters, equals, hashCode, and toString methods.
@NoArgsConstructor  // Generates a no-argument constructor.
@AllArgsConstructor // Generates a constructor initializing all fields.
@Entity // Indicates that this class is a JPA entity.
@Table(name = "Users") // Maps this entity to the "Users" table in the database.
public class User {

    @Id // Marks this field as the primary key.
    // Configures auto-increment behavior for the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // primary key
    @Column(name = "PK_users", updatable = false, nullable = false)
    private Long userId;

    // Specifies the username column, enforcing uniqueness.
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    // Specifies the password column. Consider encrypting this field in a real application.
    @Column(name = "password", nullable = false)
    private String password;

    // Specifies the position column, indicating the user's role or job title.
    @Column(name = "position", nullable = false)
    private String position;
}