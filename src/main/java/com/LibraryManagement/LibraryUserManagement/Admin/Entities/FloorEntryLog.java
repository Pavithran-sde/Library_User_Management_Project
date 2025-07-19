package com.LibraryManagement.LibraryUserManagement.Admin.Entities;


import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "floor_entry_log", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "loggedInTime"})
})
@NoArgsConstructor
@AllArgsConstructor
public class FloorEntryLog {

    public FloorEntryLog(User user, Floor floor, LocalDateTime loggedInTime, LocalDateTime loggedOutTime){
       this.user = user;
       this.floor = floor;
       this.loggedInTime = loggedInTime;
       this.loggedOutTime = loggedOutTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "floor_id")
    private Floor floor;

    @Column(nullable = false)
    private LocalDateTime loggedInTime;

    @Column()
    private LocalDateTime loggedOutTime;

    @JsonIgnore
    @Version
    private long version;

}
