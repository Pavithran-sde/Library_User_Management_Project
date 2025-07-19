package com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor;


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
public class FloorRequestDto {

    @NotBlank
    private String floorName;

    @NotBlank
    private String wing;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    private boolean active;

    private RolesEnum rolesAllowed;

    private boolean isSystemReserved;
}
