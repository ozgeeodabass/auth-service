package com.example.authservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import java.util.UUID;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
@Data
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@Builder
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false, length = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String gender;
    private String name;
    private String phoneNumber;
    private boolean deleted;

    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @Column(name = "role",nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole role;

    public User(UUID id, String username, String email, String password, String gender, String name, String phoneNumber, boolean deleted, ERole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.deleted = deleted;
        this.role = role;
    }

    public User(String username, String email, String password, String gender, String name, String phoneNumber, ERole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}

