package com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargingPortBookingRequestDto {

    private long userId;

    private long chargingPortId;

    private double userLatitude;

    private double userLongitude;
}
