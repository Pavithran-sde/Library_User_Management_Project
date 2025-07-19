package com.LibraryManagement.LibraryUserManagement.Admin.Mappers;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorRecommendationResponseDto;
import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Recommendation.RecommendationResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    RecommendationResponseDto toDto(FloorRecommendationResponseDto floorRecommendationResponseDto,
                                    String subjects);

}
