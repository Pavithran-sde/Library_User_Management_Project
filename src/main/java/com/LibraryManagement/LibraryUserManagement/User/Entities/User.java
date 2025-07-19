package com.LibraryManagement.LibraryUserManagement.User.Entities;


import com.LibraryManagement.LibraryUserManagement.Admin.Entities.FloorEntryLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryLog;
import com.LibraryManagement.LibraryUserManagement.User.Enum.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank(message = "Field cannot be blank")
    private String name;

    @Column(nullable = false)
    @NotBlank
    private String phoneNo;

    @Column(nullable = false)
    @NotBlank(message = "Field cannot be blank")
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Field cannot be blank")
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private UserCredentials userCredentials;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<TableBooking> tableBookingList;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<WaitingList_Table> waitingListTableList;


    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ChargingPortBooking> chargingPortBookingList;


    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<WaitingList_ChargingPort> waitingListChargingPorts;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<LibraryEntryLog> libraryEntryLogList;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<FloorEntryLog> floorEntryLogs;
}
