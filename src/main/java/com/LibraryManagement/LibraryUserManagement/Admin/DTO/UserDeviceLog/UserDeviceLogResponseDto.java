package com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog;

import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice.UserDeviceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceLogResponseDto {

    private long id;

    private UserDeviceResponseDto userDeviceDetails;

    private LocalDateTime logInTime;

    private LocalDateTime logOutTime;

}
