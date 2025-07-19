package com.LibraryManagement.LibraryUserManagement.User.Entities;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "tables")
@ToString(exclude = "tableBookingList")
public class Tables {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status = BookingStatusEnum.AVAILABLE;

    @JsonIgnore
    @OneToMany(mappedBy = "tables")
    private List<TableBooking> tableBookingList;

    @Version
    private long version;

    public String getBookingStatus(){
        return this.status.toString();
    }
}
