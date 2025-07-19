package com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos;


import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.Booking_UserInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.UserDevices;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryExitDto {

    @NotNull
    private Booking_UserInfoDto user;

    private boolean valid;

    @NotBlank
    private String activity;

    private long entryNumber;

}
