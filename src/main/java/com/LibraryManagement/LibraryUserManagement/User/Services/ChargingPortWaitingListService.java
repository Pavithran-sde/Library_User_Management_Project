package com.LibraryManagement.LibraryUserManagement.User.Services;

import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ChargingPortWaitingListRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPort;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPortBooking;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Entities.WaitingList_ChargingPort;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.AlreadyHasAWaitingList;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NoContentFoundException;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.NotFoundException;
import com.LibraryManagement.LibraryUserManagement.Common.Redis.Resources.RedisResourceLock;
import com.LibraryManagement.LibraryUserManagement.User.Repository.ChargingPortBookingRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.ChargingPortRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.ChargingPortWaitingListRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ChargingPortWaitingListService {

    @Autowired
    private ChargingPortWaitingListRepository chargingPortWaitingListRepository;
    @Autowired
    private ChargingPortRepository chargingPortRepository;
    @Autowired
    private ChargingPortBookingRepository chargingPortBookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisResourceLock redisResourceLock;
    @Autowired
    private NotificationService notificationService;


    public List<WaitingList_ChargingPort> getAllWL() throws Exception {
        List<WaitingList_ChargingPort> waitingListChargingPorts = chargingPortWaitingListRepository.findAll();
        if(waitingListChargingPorts.size() != 0){
            return waitingListChargingPorts;
        }
        throw new NoContentFoundException();
    }


    public WaitingList_ChargingPort getWLById(long id) throws Exception {
        return chargingPortWaitingListRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    public long getActiveWLSize(WaitingListEnum status){
        List<WaitingList_ChargingPort> list = chargingPortWaitingListRepository.findByWaitingListStatus(status);
        if(list.isEmpty()){
            return 0;
        }
        return list.size();
    }

    public long getActiveWLSizeByFloorIds(WaitingListEnum status, String floorName){
        Long activeWlSizeByFloor = chargingPortWaitingListRepository.findCountByFloorPreferenceAndWaitingListStatus(floorName, status.name());
        if(activeWlSizeByFloor == null) return 0;
        return activeWlSizeByFloor;
    }


    public List<WaitingList_ChargingPort> getWlByUserId(long id) throws Exception {
        List<WaitingList_ChargingPort> WLList = chargingPortWaitingListRepository.findByUserId(id);
        if(WLList.isEmpty()){
            throw new NoContentFoundException();
        }
        return WLList;
    }

    @Transactional
    public WaitingList_ChargingPort clearWLByUserId(ChargingPortWaitingListRequestDto chargingPortWaitingListRequestDto) throws Exception {
        WaitingList_ChargingPort waitingListChargingPort = chargingPortWaitingListRepository.findByUserIdAndWaitingListStatus(chargingPortWaitingListRequestDto.getUserId(), WaitingListEnum.WAITLISTED);
        if(waitingListChargingPort == null){
            throw new NotFoundException();
        }
        waitingListChargingPort.setWaitingListStatus(WaitingListEnum.CANCELED);
        waitingListChargingPort.setWLexitTime(LocalDateTime.now());
        return chargingPortWaitingListRepository.save(waitingListChargingPort);
    }

    @Transactional
    public long getUserToAllocateChargingPort(String floorName) throws Exception{
            WaitingList_ChargingPort waitingListChargingPort = chargingPortWaitingListRepository.findFirstByFloorPreferenceAndWaitingListStatusOrderByWLentryTimeAsc(floorName, WaitingListEnum.WAITLISTED);
            if(waitingListChargingPort != null)
                return waitingListChargingPort.getUser().getId();
            else return -1;
    }

    @Transactional
    public long getWLSize(){
        return chargingPortWaitingListRepository.findAll().size();
    }

    //**********add one more method to find the waiting list size specific for the floor
    //********************

    //=========for now it has the return type as void later update it to needs
    @Transactional
    public void allocateChargingPort(ChargingPort chargingPort, double duration){
        try{
            long userId = getUserToAllocateChargingPort(chargingPort.getFloor().getFloorName());

            if(userId != -1){
                   User user = userRepository.findById(userId).orElse(null);

                   //allocating the user a ChargingPort
                    boolean locked = false;
                    if(!redisResourceLock.isLocked("cport", chargingPort.getId())){
                        locked = redisResourceLock.tryLock("cport", chargingPort.getId(), user.getId()+"", Duration.of(5, ChronoUnit.MINUTES));
                    }
                    if(locked) {
                        ChargingPortBooking cpb = new ChargingPortBooking();
                        cpb.setChargingPort(chargingPort);
                        cpb.setUser(user);
                        cpb.setStatus(BookingStatusEnum.BOOKED);
                        cpb.setReservationStartTime(LocalDateTime.now());
                        cpb.setReservationEndTime(LocalDateTime.now().plusMinutes((long)duration));
                        chargingPort.setStatus(BookingStatusEnum.BOOKED);
                        chargingPortRepository.save(chargingPort);
                        chargingPortBookingRepository.save(cpb);

                        redisResourceLock.releaseLock("cport", chargingPort.getId(), user.getId() + "");

                        WaitingList_ChargingPort waitingListChargingPort = chargingPortWaitingListRepository.findByUserIdAndWaitingListStatus(user.getId(), WaitingListEnum.WAITLISTED);
                        waitingListChargingPort.setWaitingListStatus(WaitingListEnum.BOOKED);
                        waitingListChargingPort.setWLexitTime(LocalDateTime.now());
                        chargingPortWaitingListRepository.save(waitingListChargingPort);
                        //notificationService.sendCPBookingNotification(cpb);
                    } else {
                        System.out.println("ResourceAlreadyBooked");
                    }
            }
        } catch (Exception e){
            //do nothing
            e.printStackTrace();
        }

    }

    public void updateBookingStatus(){

    }

    public void addUsertoWL(User user, String floorName) throws Exception{
        if(!AlreadyExist(user)){
            WaitingList_ChargingPort waitingListChargingPort = new WaitingList_ChargingPort();
            waitingListChargingPort.setUser(user);
            waitingListChargingPort.setWaitingListStatus(WaitingListEnum.WAITLISTED);
            waitingListChargingPort.setFloorPreference(floorName);
            waitingListChargingPort.setWLentryTime(LocalDateTime.now());
            waitingListChargingPort.setWLexitTime(null);
            chargingPortWaitingListRepository.save(waitingListChargingPort);
        }
        else throw new AlreadyHasAWaitingList("Charging port");
    }

    private boolean AlreadyExist(User user) {
    WaitingList_ChargingPort existingEntry = chargingPortWaitingListRepository.findByUserIdAndWaitingListStatus(user.getId(), WaitingListEnum.WAITLISTED);
        if(existingEntry == null ) return false;
        else return true;
    }


}
