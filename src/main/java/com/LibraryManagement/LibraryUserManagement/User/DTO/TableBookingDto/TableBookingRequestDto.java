package com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto;

import com.LibraryManagement.LibraryUserManagement.User.Enum.UserFloorPreferenceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableBookingRequestDto {

    private long userId;

    private long tablesId;

    private UserFloorPreferenceEnum preference;

    private double userLatitude;

    private double userLongitude;

}
