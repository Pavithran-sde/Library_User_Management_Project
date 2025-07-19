package com.LibraryManagement.LibraryUserManagement.User.Repository;


import com.LibraryManagement.LibraryUserManagement.User.Entities.WaitingList_Table;
import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableWaitingListRepository extends JpaRepository<WaitingList_Table, Long> {
    List<WaitingList_Table> findByUserId(long user_id);
    WaitingList_Table findByUserIdAndWaitingListStatus(long user_id, WaitingListEnum status);
    WaitingList_Table findFirstByWaitingListStatusOrderByWLentryTimeAsc(WaitingListEnum status);
    WaitingList_Table findFirstByWaitingListStatusAndUserFloorPreferenceEnumOrderByWLentryTimeAsc(WaitingListEnum status, String preference);
}
