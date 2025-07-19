package com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionResponseDto {

    private long id;

    private String sectionName;

    private FloorResponseDto floorDetails;

    private SectionAvailablityEnum isAvailable;
}
