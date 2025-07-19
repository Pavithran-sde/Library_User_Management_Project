package com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto;

import com.LibraryManagement.LibraryUserManagement.User.DTO.TableDtos.TableWithStatusInfoDto;
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
public class TableBookingByUserDto {
    private long id;

    private Booking_UserInfoDto user;

    private TableWithStatusInfoDto tables;

    private BookingStatusEnum status;

    private LocalDateTime reservationStartTime;

    private LocalDateTime reservationEndTime;

    private String reservedDuration;

}
