package com.LibraryManagement.LibraryUserManagement.User.Mappers;

import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.Booking_UserInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.UserCredentialDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.UserInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Entities.UserCredentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoDto toUserInfoDto(User user);

    Booking_UserInfoDto toBooking_UserInfoDto(User user);

    User fromDto(UserInfoDto userInfoDtoDto);

    UserCredentials fromDto(UserCredentialDto userCredentialDto);
}
