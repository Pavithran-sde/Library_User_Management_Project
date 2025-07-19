package com.LibraryManagement.LibraryUserManagement.User.Entities;

import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Data
@Entity
@Getter
@Setter
@Table(name="table_reservation")
@NoArgsConstructor
public class TableBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tables_id", nullable = false)
    private Tables tables;

    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status;

    @Column(name="booking_time", nullable = false)
    private LocalDateTime reservationStartTime;

    @Column(name="leaving_time")
    private LocalDateTime reservationEndTime;

}
