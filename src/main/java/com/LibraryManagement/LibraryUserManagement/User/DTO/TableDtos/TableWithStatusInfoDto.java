package com.LibraryManagement.LibraryUserManagement.User.DTO.TableDtos;

import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableWithStatusInfoDto {

    private long id;

    private String floorName;

    private String wing;

    @JsonProperty("Table_Booking_Status")
    private BookingStatusEnum bookingStatusEnum;
}
