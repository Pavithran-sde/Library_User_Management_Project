package com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto;


import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortDto.ChargingPortWithStatusInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.Booking_UserInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargingPortBookingResponseDto {
    @JsonProperty("Booking_No:")
    private long id;

    @JsonProperty("User_Id")
    private long userId;

    private Booking_UserInfoDto user;

    @JsonProperty("Charging_Port_Id")
    private long chargingPortId;

    private ChargingPortWithStatusInfoDto chargingPort;

    @JsonProperty("Booking_Status")
    private BookingStatusEnum status;

    private LocalDateTime reservationStartTime;

}
