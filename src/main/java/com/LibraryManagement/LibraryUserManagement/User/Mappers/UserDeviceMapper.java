package com.LibraryManagement.LibraryUserManagement.User.Mappers;

import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice.LibraryExitDeviceInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice.UserDeviceRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice.UserDeviceResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.UserDevices;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDeviceMapper {

    UserDevices fromDto(UserDeviceRequestDto dto);

    UserDeviceResponseDto toDto(UserDevices userDevices);

    LibraryExitDeviceInfoDto toExitDto(UserDevices userDevices);

}
