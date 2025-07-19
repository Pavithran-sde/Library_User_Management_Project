package com.LibraryManagement.LibraryUserManagement.User.DTO.TableDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfoDto {

  @NotBlank
 private String floorName;

  @NotBlank
 private String wing;




}
