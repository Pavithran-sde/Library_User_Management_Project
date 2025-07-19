package com.LibraryManagement.LibraryUserManagement.Admin.Repository;

import com.LibraryManagement.LibraryUserManagement.Admin.Entities.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Long> {

    @Query(nativeQuery = true,
            value ="SELECT * FROM floor WHERE floor_name =:floorName AND wing =:wing AND is_system_reserved = false" )
    Floor findByFloorNameAndWing(@Param("floorName") String floorName,@Param("wing") String wing);

    Floor findByIsSystemReserved(Boolean reserved);

    List<Floor> findAllByIsSystemReserved(boolean isSystemReserved);

    List<Floor> findByFloorName(String floorName);
}
