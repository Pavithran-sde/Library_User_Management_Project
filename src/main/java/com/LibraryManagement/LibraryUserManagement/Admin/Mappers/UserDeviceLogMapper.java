package com.LibraryManagement.LibraryUserManagement.Admin.Mappers;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog.UserDeviceLogRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog.UserDeviceLogResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.UserDeviceLog;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice.LibraryExitDeviceInfoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDeviceLogMapper {

    UserDeviceLogResponseDto toDto(UserDeviceLog userDeviceLog);

    UserDeviceLog fromDto(UserDeviceLogRequestDto requestDto);

}
