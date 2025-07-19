package com.LibraryManagement.LibraryUserManagement.Admin.DTO.QR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRForDateDto {
    private LocalDateTime QRDate;
}
