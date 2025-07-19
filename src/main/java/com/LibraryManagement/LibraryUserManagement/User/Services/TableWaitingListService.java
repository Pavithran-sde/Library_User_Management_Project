package com.LibraryManagement.LibraryUserManagement.User.Services;


import com.LibraryManagement.LibraryUserManagement.User.DTO.TableWaitingList.TableWaitingRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.TableBooking;
import com.LibraryManagement.LibraryUserManagement.User.Entities.Tables;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Entities.WaitingList_Table;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.UserFloorPreferenceEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.AlreadyHasAWaitingList;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.Common.Redis.Resources.RedisResourceLock;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableBookingRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableWaitingListRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TableWaitingListService {

    @Autowired
    private TableWaitingListRepository tableWaitingListRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private TableBookingRepository tableBookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisResourceLock redisResourceLock;
    @Autowired
    private NotificationService notificationService;

    public List<WaitingList_Table> getAllWL() throws Exception {
        List<WaitingList_Table> waitingListTableList = tableWaitingListRepository.findAll();
        if(waitingListTableList.size() != 0){
            return waitingListTableList;
        }
        throw new NoContentFoundException();
    }


    public WaitingList_Table getWLById(long id) throws Exception {
        return tableWaitingListRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }


    public List<WaitingList_Table> getWlByUserId(long id) throws Exception {
        List<WaitingList_Table> WLList = tableWaitingListRepository.findByUserId(id);
        if(WLList.isEmpty()){
            throw new NoContentFoundException();
        }
        return WLList;
    }

    @Transactional
    public WaitingList_Table clearWLByUserId(TableWaitingRequestDto tableWaitingRequestDto) throws Exception {
        WaitingList_Table waitingListTable = tableWaitingListRepository.findByUserIdAndWaitingListStatus(tableWaitingRequestDto.getUserId(), WaitingListEnum.WAITLISTED);
        if(waitingListTable == null){
            throw new NotFoundException();
        }
        waitingListTable.setWaitingListStatus(WaitingListEnum.CANCELED);
        waitingListTable.setWLexitTime(LocalDateTime.now());
        return tableWaitingListRepository.save(waitingListTable);
    }

    @Transactional
    public long getUserToAllocateTable() throws Exception{
        if(getWLSize() != 0){
            WaitingList_Table waitingListTable = tableWaitingListRepository.findFirstByWaitingListStatusOrderByWLentryTimeAsc(WaitingListEnum.WAITLISTED);
            if(waitingListTable != null)
                return waitingListTable.getUser().getId();
            else{
                return -1;
            }
        }

        throw new NoContentFoundException();

    }

    @Transactional
    public long getWLSize(){
        return tableWaitingListRepository.findAll().size();
    }



    //=========for now it has the return type as void later update it to needs
    @Transactional
    public void allocateTable(Tables tables){
        try{
            long userId = getUserToAllocateTable();
            if(userId != -1) {
                WaitingList_Table wL = tableWaitingListRepository.findByUserIdAndWaitingListStatus(userId, WaitingListEnum.WAITLISTED);
                User user = null;
                if (wL.getUserFloorPreferenceEnum().equals(tables.getFloor().getFloorName())
                        || wL.getUserFloorPreferenceEnum().equals("ANY")) {
                    user = userRepository.findById(userId).orElse(null);
                } else {
                    WaitingList_Table wl2 = tableWaitingListRepository.findFirstByWaitingListStatusAndUserFloorPreferenceEnumOrderByWLentryTimeAsc(WaitingListEnum.WAITLISTED, "ANY");
                    WaitingList_Table wl3 = tableWaitingListRepository.findFirstByWaitingListStatusAndUserFloorPreferenceEnumOrderByWLentryTimeAsc(WaitingListEnum.WAITLISTED, tables.getFloor().getFloorName());

                    if (wl3 == null && wl2 != null) {
                        user = wl2.getUser();
                    } else if (wl2 == null && wl3 != null) {
                        user = wl3.getUser();
                    } else if (wl2 != null && wl3 != null) {
                        if (wl2.getWLentryTime().isBefore(wl3.getWLentryTime())) {
                            user = wl2.getUser();
                        } else {
                            user = wl3.getUser();
                        }
                    } else {
                        user = null;
                    }

                }
                //allocating the user a table
//=================add preference logic to the code==============================
                if (user != null) {

                    boolean locked = false;
                    if (!redisResourceLock.isLocked("table", tables.getId())) {
                        locked = redisResourceLock.tryLock("table", tables.getId(), user.getId() + "", Duration.of(5, ChronoUnit.MINUTES));
                    }
                    if (locked) {
                        TableBooking tb = new TableBooking();
                        tb.setTables(tables);
                        tb.setUser(user);
                        tb.setStatus(BookingStatusEnum.BOOKED);
                        tb.setReservationStartTime(LocalDateTime.now());
                        tb.setReservationEndTime(null);
                        tables.setStatus(BookingStatusEnum.BOOKED);
                        tableRepository.save(tables);
                        tableBookingRepository.save(tb);

                        redisResourceLock.releaseLock("table", tables.getId(), user.getId() + "");

                        WaitingList_Table waitingListTable = tableWaitingListRepository.findByUserIdAndWaitingListStatus(user.getId(), WaitingListEnum.WAITLISTED);
                        waitingListTable.setWaitingListStatus(WaitingListEnum.BOOKED);
                        waitingListTable.setWLexitTime(LocalDateTime.now());

                        tableWaitingListRepository.save(waitingListTable);
                        notificationService.sendTableBookingNotification(tb);
                    } else {
                        System.out.println("ResourceAlreadyBooked");
                    }
                }
            }
            } catch (Exception e){
                //do nothing
                //e.printStackTrace();
            }
    }

    public void updateBookingStatus(){

    }

    public void addUsertoWL(User user, String floorName, UserFloorPreferenceEnum preferenceEnum) throws Exception {
        if(AlreadyExist(user)){
            WaitingList_Table waitingListTable = new WaitingList_Table();
            waitingListTable.setUser(user);
            waitingListTable.setWaitingListStatus(WaitingListEnum.WAITLISTED);
            if(preferenceEnum.equals(UserFloorPreferenceEnum.SAME)) waitingListTable.setUserFloorPreferenceEnum(floorName);
            else waitingListTable.setUserFloorPreferenceEnum(UserFloorPreferenceEnum.ANY.toString());
            waitingListTable.setWLentryTime(LocalDateTime.now());
            waitingListTable.setWLexitTime(null);
            tableWaitingListRepository.save(waitingListTable);
        }
        else throw new AlreadyHasAWaitingList("table");
    }

    private boolean AlreadyExist(User user) {
       WaitingList_Table existingEntry =  tableWaitingListRepository.findByUserIdAndWaitingListStatus(user.getId(), WaitingListEnum.WAITLISTED);
        if(existingEntry == null ) return false;
        else return true;
    }
}
