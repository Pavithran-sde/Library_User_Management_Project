package com.LibraryManagement.LibraryUserManagement.Admin.DTO.QR;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryEntryQRScannerDto {
    private LocalDate date;
    private boolean isValid;
}
