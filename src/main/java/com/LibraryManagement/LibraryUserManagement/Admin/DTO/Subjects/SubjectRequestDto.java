package com.LibraryManagement.LibraryUserManagement.Admin.DTO.Subjects;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto.SectionResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequestDto {
    @NotBlank
    private String subjectName;

    @NotBlank
    private String sectionName;

    @NotNull
    private SectionAvailablityEnum isAvailable;
}
