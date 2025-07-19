package com.LibraryManagement.LibraryUserManagement.User.Entities;


import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="chargingPort_booking")
public class ChargingPortBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chargingport_id", nullable = false)
    private ChargingPort chargingPort;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatusEnum status;

    @Column(name="booking_time", nullable = false)
    private LocalDateTime reservationStartTime;

    @Column(name="leaving_time")
    private LocalDateTime reservationEndTime;
}
