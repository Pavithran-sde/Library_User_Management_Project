package com.LibraryManagement.LibraryUserManagement.Admin.DTO.Recommendation;

import com.LibraryManagement.LibraryUserManagement.Admin.DTO.Floor.FloorRecommendationResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationResponseDto {

    private FloorRecommendationResponseDto floorRecommendationResponseDto;

    private String subjects;

}
