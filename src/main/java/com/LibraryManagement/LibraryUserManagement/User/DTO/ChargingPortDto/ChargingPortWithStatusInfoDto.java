package com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortDto;

import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargingPortWithStatusInfoDto {

    private long id;

    private String floorName;

    private String wing;

    @JsonProperty("Charging_Port_Booking_Status")
    private BookingStatusEnum status;

}
