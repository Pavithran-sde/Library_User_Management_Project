package com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto;

import com.LibraryManagement.LibraryUserManagement.User.Enum.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private long id;
    private String name;
    private String phoneNo;
    private String email;
    private GenderEnum gender;
}
