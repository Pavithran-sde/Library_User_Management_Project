package com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice;

import com.LibraryManagement.LibraryUserManagement.User.Enum.DeviceTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryExitDeviceInfoDto {
    @NotNull
    private DeviceTypeEnum deviceTypeEnum;

    @NotBlank
    private String manufacturer;

    @NotBlank
    private String modelName;

    @NotBlank
    private String colour;
}
