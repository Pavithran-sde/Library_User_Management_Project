package com.LibraryManagement.LibraryUserManagement.User.Repository;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import com.LibraryManagement.LibraryUserManagement.User.Entities.Tables;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface TableRepository extends JpaRepository<Tables, Long> {

    List<Tables> findByFloorIdIn(List<Long> floorIds);

    List<Tables> findByStatus(BookingStatusEnum status);

}
