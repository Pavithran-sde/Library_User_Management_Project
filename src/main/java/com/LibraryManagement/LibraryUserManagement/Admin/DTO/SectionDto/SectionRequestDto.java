package com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto;


import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionRequestDto {

    @NotBlank
    private String sectionName;

    @NotBlank
    private String floorName;

    @NotBlank
    private String wing;

    @NotNull
    private SectionAvailablityEnum isAvailable;

}
