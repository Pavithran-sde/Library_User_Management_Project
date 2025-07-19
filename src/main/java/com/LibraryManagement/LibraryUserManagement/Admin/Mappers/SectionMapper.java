package com.LibraryManagement.LibraryUserManagement.Admin.Mappers;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto.SectionRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.SectionDto.SectionResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Sections;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectionMapper {

    SectionResponseDto toDto(Sections sections);

    Sections fromDto(SectionRequestDto sectionRequestDto);

}
