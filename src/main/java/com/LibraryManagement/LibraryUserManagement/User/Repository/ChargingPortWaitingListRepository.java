package com.LibraryManagement.LibraryUserManagement.User.Repository;

import com.LibraryManagement.LibraryUserManagement.User.Entities.WaitingList_ChargingPort;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChargingPortWaitingListRepository extends JpaRepository<WaitingList_ChargingPort, Long> {
    List<WaitingList_ChargingPort> findByUserId(long user_id);
    WaitingList_ChargingPort findByUserIdAndWaitingListStatus(long user_id, WaitingListEnum status);
    WaitingList_ChargingPort findFirstByWaitingListStatusOrderByWLentryTimeAsc(WaitingListEnum status);
    WaitingList_ChargingPort findFirstByFloorPreferenceAndWaitingListStatusOrderByWLentryTimeAsc(String floor, WaitingListEnum status);
    List<WaitingList_ChargingPort> findByWaitingListStatus(WaitingListEnum status);

    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) FROM waiting_list_charging_port WHERE floor_preference =:floorName AND waiting_list_status =:waitingListEnum")
    Long findCountByFloorPreferenceAndWaitingListStatus(@Param("floorName") String floorName,@Param("waitingListEnum") String waitingListEnum);
}
