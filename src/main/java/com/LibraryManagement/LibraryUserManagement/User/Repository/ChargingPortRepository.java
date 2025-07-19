package com.LibraryManagement.LibraryUserManagement.User.Repository;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPort;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargingPortRepository extends JpaRepository<ChargingPort, Long>{

    List<ChargingPort> findByFloorIdIn(List<Long> floorId);

    List<ChargingPort> findByStatus(BookingStatusEnum status);

}
