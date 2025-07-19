package com.LibraryManagement.LibraryUserManagement.Admin.Entities;


import com.LibraryManagement.LibraryUserManagement.Admin.Enums.RolesEnum;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPort;
import com.LibraryManagement.LibraryUserManagement.User.Entities.Tables;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "floor", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"floor_name", "wing"})
})
public class Floor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(nullable = false, name = "floor_name")
    private String floorName;


    @Column(nullable = false, name = "wing")
    private String wing;


    @Column(nullable = false)
    private String Secret;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RolesEnum rolesAllowed;

    @Column(nullable = false)
    private boolean isSystemReserved;

    @JsonIgnore
    @OneToOne(mappedBy = "floor")
    private  Sections section;

    @JsonIgnore
    @OneToMany(mappedBy = "floor")
    private List<FloorEntryLog> floorEntryLog;

    @JsonIgnore
    @OneToMany(mappedBy = "floor")
    private List<Tables> tables;

    @JsonIgnore
    @OneToMany(mappedBy = "floor")
    private List<ChargingPort> chargingPort;

}
