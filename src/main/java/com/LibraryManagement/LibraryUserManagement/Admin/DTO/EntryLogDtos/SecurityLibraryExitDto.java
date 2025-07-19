package com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos;


import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice.LibraryExitDeviceInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.Booking_UserInfoDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityLibraryExitDto {

    @NotNull
    private long libraryEntryId;

    @NotNull
    private Booking_UserInfoDto user;

    @NotNull
    private List<LibraryExitDeviceInfoDto> libraryExitDeviceInfoDtoList;

    private long NoOfDevices;
}
