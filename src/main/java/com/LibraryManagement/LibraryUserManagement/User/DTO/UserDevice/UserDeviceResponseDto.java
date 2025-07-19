package com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice;


import com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto.Booking_UserInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Enum.DeviceLogEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.DeviceTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceResponseDto {

    private long id;

    private DeviceTypeEnum deviceTypeEnum;

    private String manufacturer;

    private String modelName;

    private String colour;

    private Booking_UserInfoDto userInfo;

    private DeviceLogEnum isActive;

}
