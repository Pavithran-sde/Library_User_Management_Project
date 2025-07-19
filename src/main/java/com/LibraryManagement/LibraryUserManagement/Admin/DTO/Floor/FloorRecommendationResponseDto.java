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
public class FloorRecommendationResponseDto {

    @NotBlank
    private String floorName;

    @NotBlank
    private String wing;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    private boolean isActive;

    private RolesEnum rolesAllowed;

    private long totalTable;

    private long availableTable;

    private long occupiedTable;

    private long totalCp;

    private long availableCp;

    private long occupiedCp;

}
