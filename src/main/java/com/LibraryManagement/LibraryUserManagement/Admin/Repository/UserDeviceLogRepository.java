package com.LibraryManagement.LibraryUserManagement.Admin.Repository;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.UserDeviceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserDeviceLogRepository extends JpaRepository<UserDeviceLog, Long> {

    List<UserDeviceLog> findByUserDevicesId(long id);

    UserDeviceLog findByUserDevicesIdAndLogOutTime(long deviceId, LocalDateTime localDateTime);

    @Query(nativeQuery = true,
            value ="SELECT * FROM user_device_log WHERE library_entry_id =:entryId")
    List<UserDeviceLog> findByLibraryEntryId(@Param("entryId") long entryId);

}
