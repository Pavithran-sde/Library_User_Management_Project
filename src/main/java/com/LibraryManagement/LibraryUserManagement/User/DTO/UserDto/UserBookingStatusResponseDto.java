package com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBookingStatusResponseDto {

    @JsonProperty("Table_Status")
    private String tableBookingStatus;

    @JsonProperty("Charging_port_Status")
    private String chargingPortBookingStatus;
}
