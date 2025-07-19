package com.LibraryManagement.LibraryUserManagement.User.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "user_credentials")
@ToString(exclude = "user")
public class UserCredentials {

    @Id
    private long id;

    @Column(name="user_name", nullable = false, unique = true)
    @NotBlank
    private String userName;

    @Column(name="user_password", nullable = false)
    @NotBlank
    private String userPassword;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private boolean isEnabled;

    @Column(nullable = false)
    private boolean isAccountExpired;

    @Column(nullable = false)
    private boolean isBlocked;

    @Column(nullable = false)
    private String role;

}
