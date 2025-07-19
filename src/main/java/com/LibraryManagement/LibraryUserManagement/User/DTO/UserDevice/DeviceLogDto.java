package com.LibraryManagement.LibraryUserManagement.User.DTO.UserDevice;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceLogDto {

    private long deviceId;
    @NotNull
    private LocalDateTime entryTime;
    @NotNull
    private LocalDateTime exitTime;
}
