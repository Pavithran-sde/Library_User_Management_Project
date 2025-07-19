package com.LibraryManagement.LibraryUserManagement.User.Mappers;

import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.TableBookingByUserDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.TableBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.TableBookingResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.TableBooking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TableBookingMapper {

    TableBookingByUserDto toDto(TableBooking tableBooking);

    TableBookingResponseDto toResponseDto(TableBooking tableBooking);

    TableBooking fromTableBookingDto(TableBookingDto tableBookingDto);

    TableBookingDto toTableBookingDto(TableBooking tableBooking);


}
