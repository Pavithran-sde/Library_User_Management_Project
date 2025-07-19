package com.LibraryManagement.LibraryUserManagement.Admin.Repository;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryQrCodes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface LibraryEntryQrTokenLogRepository extends JpaRepository<LibraryEntryQrCodes, Long> {

    LibraryEntryQrCodes findByDate(LocalDate date);
}
