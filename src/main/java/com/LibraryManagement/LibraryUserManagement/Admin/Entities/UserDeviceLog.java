package com.LibraryManagement.LibraryUserManagement.Admin.Entities;


import com.LibraryManagement.LibraryUserManagement.User.Entities.UserDevices;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "library_entry_id")
    private LibraryEntryLog libraryEntryLog;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userDevices_id")
    private UserDevices userDevices;

    @Column(nullable = false)
    private LocalDateTime logInTime;

    @Column
    private LocalDateTime logOutTime;

    @Version
    private long version;
}
