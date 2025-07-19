package com.LibraryManagement.LibraryUserManagement.User.DTO.TableDtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking_TableInfoDto {

    @JsonProperty("Table_id")
    private long id;

    private String floorName;

    private String wing;
}
