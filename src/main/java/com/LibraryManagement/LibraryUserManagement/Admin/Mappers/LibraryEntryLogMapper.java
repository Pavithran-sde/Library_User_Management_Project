package com.LibraryManagement.LibraryUserManagement.Admin.Mappers;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.EntryResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.EntryLogDtos.LibraryEntryResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibraryEntryLogMapper {

    LibraryEntryResponseDto toDto(LibraryEntryLog log);

}
