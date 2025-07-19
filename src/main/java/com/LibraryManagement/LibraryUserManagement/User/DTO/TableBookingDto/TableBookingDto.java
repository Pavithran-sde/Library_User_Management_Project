package com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto;


import com.LibraryManagement.LibraryUserManagement.User.Entities.Tables;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableBookingDto {

    private User user;

    private Tables tables;

    private BookingStatusEnum status;

    private LocalDateTime reservationStartTime;

    private LocalDateTime reservationEndTime;


}
