package com.LibraryManagement.LibraryUserManagement.Admin.DTO.UserDeviceLog;

import com.LibraryManagement.LibraryUserManagement.User.Entities.UserDevices;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeviceLogRequestDto {

    private long deviceId;

    private long userId;

    private long libraryEntryNo;
}
