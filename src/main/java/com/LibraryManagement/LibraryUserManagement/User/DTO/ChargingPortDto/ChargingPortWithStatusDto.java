package com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortDto;


import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargingPortWithStatusDto {
    private long id;
    private String floorName;
    private String wing;
    private BookingStatusEnum status;
}
