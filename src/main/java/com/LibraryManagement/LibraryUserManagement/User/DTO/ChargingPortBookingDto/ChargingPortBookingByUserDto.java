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
public class ChargingPortBookingByUserDto {

    private long id;

    private Booking_UserInfoDto user;

    private ChargingPortWithStatusInfoDto chargingPort;

    private BookingStatusEnum status;

    private LocalDateTime reservationStartTime;

    private LocalDateTime reservationEndTime;

    private String reservedDuration;
}
