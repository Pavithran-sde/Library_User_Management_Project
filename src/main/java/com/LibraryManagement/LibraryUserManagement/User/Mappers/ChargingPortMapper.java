package com.LibraryManagement.LibraryUserManagement.User.Mappers;

import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortDto.ChargingPortDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortDto.ChargingPortWithStatusInfoDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPort;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChargingPortMapper {

        ChargingPort fromDto(ChargingPortDto chargingPortDto);
        ChargingPortWithStatusInfoDto toDto(ChargingPort chargingPort);

}
