package com.LibraryManagement.LibraryUserManagement.User.Mappers;

import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ChargingPortBookingByUserDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ChargingPortBookingResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPortBooking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChargingPortBookingMapper {

    ChargingPortBookingResponseDto toResponseDto(ChargingPortBooking chargingPortBooking);
    ChargingPortBookingByUserDto toDto(ChargingPortBooking chargingPortBooking);

}
