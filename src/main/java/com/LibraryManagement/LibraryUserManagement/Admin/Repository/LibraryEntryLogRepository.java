package com.LibraryManagement.LibraryUserManagement.Admin.Repository;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.LibraryEntryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LibraryEntryLogRepository extends JpaRepository<LibraryEntryLog, Long> {
    LibraryEntryLog findByUserIdAndLoggedOutTime(long id, LocalDateTime time);

    Boolean existsById(long id);
}
