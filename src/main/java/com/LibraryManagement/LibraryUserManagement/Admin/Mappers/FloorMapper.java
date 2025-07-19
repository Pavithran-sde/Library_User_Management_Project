package com.LibraryManagement.LibraryUserManagement.Admin.Mappers;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorRecommendationResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorRequestDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FloorMapper {

    FloorResponseDto toDto(Floor floor);

    Floor fromDto(FloorRequestDto floorRequestDto);

    FloorRecommendationResponseDto toRecomDto(Floor floor);

}
