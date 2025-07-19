package com.LibraryManagement.LibraryUserManagement.User.Entities;


import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "charging_ports")
public class ChargingPort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @JsonIgnore
    @OneToMany(mappedBy = "chargingPort")
    private List<ChargingPortBooking> bookings;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status = BookingStatusEnum.AVAILABLE;

    public String getBookingStatus(){
        return this.status.toString();
    }
}
