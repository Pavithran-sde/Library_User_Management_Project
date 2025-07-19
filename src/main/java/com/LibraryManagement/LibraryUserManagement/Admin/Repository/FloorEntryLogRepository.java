package com.LibraryManagement.LibraryUserManagement.Admin.Repository;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.FloorEntryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FloorEntryLogRepository extends JpaRepository<FloorEntryLog, Long> {

    FloorEntryLog findByUserIdAndLoggedOutTime(long id, LocalDateTime time);
}
