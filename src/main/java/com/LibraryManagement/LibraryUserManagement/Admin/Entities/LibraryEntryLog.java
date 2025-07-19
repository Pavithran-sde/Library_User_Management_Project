package com.LibraryManagement.LibraryUserManagement.Admin.Entities;


import com.LibraryManagement.LibraryUserManagement.Admin.Enums.LibraryLogDeviceEnum;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "library_entry_log", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "loggedInTime"})
})
@AllArgsConstructor
@NoArgsConstructor
public class LibraryEntryLog {

    public LibraryEntryLog(User user, LocalDateTime loggedInTime, LocalDateTime loggedOutTime,
                           LibraryLogDeviceEnum deviceEnum){
        this.deviceEnum = deviceEnum;
        this.user = user;
        this.loggedInTime = loggedInTime;
        this.loggedOutTime = loggedOutTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime loggedInTime;

    @Column
    private LocalDateTime loggedOutTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LibraryLogDeviceEnum deviceEnum;

    @JsonIgnore
    @Version
    private long version;

    @JsonIgnore
    @OneToMany(mappedBy = "libraryEntryLog")
    private List<UserDeviceLog> userDeviceLogList;
}
