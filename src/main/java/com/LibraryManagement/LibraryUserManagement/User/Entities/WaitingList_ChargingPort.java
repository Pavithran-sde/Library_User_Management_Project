package com.LibraryManagement.LibraryUserManagement.User.Entities;


import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
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
@Table(name = "Waiting_list_chargingPort")
public class WaitingList_ChargingPort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WaitingListEnum waitingListStatus;

    @Column(nullable = false)
    private LocalDateTime WLentryTime;

    @Column
    private LocalDateTime WLexitTime;

    @Column(nullable = false)
    private String floorPreference;

}
