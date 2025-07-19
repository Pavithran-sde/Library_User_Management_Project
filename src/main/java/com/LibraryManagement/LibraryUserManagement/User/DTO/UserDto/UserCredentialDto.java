package com.LibraryManagement.LibraryUserManagement.User.DTO.UserDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialDto {
    @NotBlank
    private String userName;
    @NotBlank
    private String userPassword;
    @NotNull
    private boolean isEnabled;
    @NotNull
    private boolean isAccountExpired;
    @NotNull
    private boolean isBlocked;
    @NotBlank
    private String role;
}
