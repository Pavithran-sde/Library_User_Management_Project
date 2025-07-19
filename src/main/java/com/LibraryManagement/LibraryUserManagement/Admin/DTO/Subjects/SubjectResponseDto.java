package com.LibraryManagement.LibraryUserManagement.Admin.DTO.Subjects;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto.SectionResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.Enum.SectionAvailablityEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponseDto {

    private long id;

    @NotBlank
    private String subjectName;

    private SectionResponseDto sectionDetails;

    private SectionAvailablityEnum isAvailable;
}
