package com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos;


import com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog.UserDeviceLogResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.UserDeviceLog;
import com.LibraryManagement.LibraryUserManagement.Admin.Enums.LibraryLogDeviceEnum;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.Booking_UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryEntryResponseDto {

    private long id;

    private Booking_UserInfoDto user;

    private LocalDateTime loggedInTime;

    private LocalDateTime loggedOutTime;

    private LibraryLogDeviceEnum deviceEnum;

    private List<UserDeviceLogResponseDto> userDevices;


}
