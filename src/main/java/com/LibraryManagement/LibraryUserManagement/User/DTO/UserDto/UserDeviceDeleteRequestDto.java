package com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceDeleteRequestDto {

    private long deviceId;

    private long userId;
}
