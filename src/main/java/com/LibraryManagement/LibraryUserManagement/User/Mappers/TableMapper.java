package com.LibraryManagement.LibraryUserManagement.User.Mappers;


import com.LibraryManagement.LibraryUserManagement.User.DTO.TableDtos.TableInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableDtos.TableWithStatusInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.Tables;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TableMapper{


    TableWithStatusInfoDto toDto(Tables tables);

    Tables fromDto(TableInfoDto tableInfoDto);



}
