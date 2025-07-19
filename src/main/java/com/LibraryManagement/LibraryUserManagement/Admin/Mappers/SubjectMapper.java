package com.LibraryManagement.LibraryUserManagement.Admin.Mappers;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Subjects.SubjectRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Subjects.SubjectResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Subjects;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectResponseDto toDto(Subjects subjects);

    Subjects fromDto(SubjectRequestDto requestDto);

}
