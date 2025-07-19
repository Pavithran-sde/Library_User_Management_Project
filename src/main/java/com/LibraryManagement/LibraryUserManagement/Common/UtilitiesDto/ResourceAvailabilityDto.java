package com.LibraryManagement.LibraryUserManagement.Common.UtilitiesDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceAvailabilityDto {

    private long totalTable;

    private long availableTable;

    private long occupiedTable;

    private long totalCp;

    private long availableCp;

    private long occupiedCp;


}
