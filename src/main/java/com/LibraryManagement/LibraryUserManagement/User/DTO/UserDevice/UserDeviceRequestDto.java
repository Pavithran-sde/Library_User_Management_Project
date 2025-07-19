package com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice;

import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Enum.DeviceLogEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.DeviceTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceRequestDto {


    @NotNull
    private DeviceTypeEnum deviceTypeEnum;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String modelName;

    @NotBlank
    private String colour;

    private long userId;

    @NotNull
    private DeviceLogEnum isActive = DeviceLogEnum.NOT_ACTIVE;

}
