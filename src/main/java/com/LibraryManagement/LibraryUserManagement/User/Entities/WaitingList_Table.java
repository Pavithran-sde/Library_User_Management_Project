package com.LibraryManagement.LibraryUserManagement.User.Entities;


import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WaitingList_Table {

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
    @NotBlank
    private String userFloorPreferenceEnum;
}
