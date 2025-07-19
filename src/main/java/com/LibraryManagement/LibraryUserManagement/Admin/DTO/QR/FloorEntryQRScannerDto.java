package com.LibraryManagement.LibraryUserManagement.Admin.DTO.QR;


import com.LibraryManagement.LibraryUserManagement.Admin.Enums.RolesEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloorEntryQRScannerDto {
    private boolean isValid;
    private String floorName;
    private String wing;
}
