package com.LibraryManagement.LibraryUserManagement.User.Services;

import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.EntryLogStatusChecker;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ChargingPortBookingByUserDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ChargingPortBookingRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ChargingPortBookingResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ClearChargingPortBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPort;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPortBooking;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.WaitingListEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.*;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.ChargingPortBookingMapper;
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
import java.util.ArrayList;
import java.util.List;


@Service
public class ChargingPortBookingService {

    @Autowired
    private ChargingPortBookingRepository chargingPortBookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChargingPortRepository chargingPortRepository;
    @Autowired
    private ChargingPortBookingMapper chargingPortBookingMapper;
    @Autowired
    private ChargingPortWaitingListService chargingPortWaitingListService;
    @Autowired
    private RedisResourceLock redisResourceLock;
    @Autowired
    private DynamicTimeAllocationService dynamicTimeAllocationService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private ChargingPortWaitingListRepository chargingPortWaitingListRepository;

    @Autowired
    private EntryLogStatusChecker entryLogStatusChecker;

    public String getBookingCount() {
        List<ChargingPortBooking> chargingPortBookingList = chargingPortBookingRepository.findByStatus(BookingStatusEnum.BOOKED);
        return "Total Booking : "+ chargingPortBookingList.size() + "/ total charging port :"+ chargingPortRepository.findAll().size();

    }

    public List<User> getAllUsersHavingBookingForToday(){
        List<ChargingPortBooking> chargingPortBookingList = chargingPortBookingRepository.findByStatus(BookingStatusEnum.BOOKED);
        return chargingPortBookingList.stream().map(chargingPortBooking -> {
           return chargingPortBooking.getUser();
        }).toList();
    }


    public List<ChargingPortBooking> getAllBookings() throws Exception {
        List<ChargingPortBooking> chargingPortBookingsList = chargingPortBookingRepository.findAll();
        if(chargingPortBookingsList.size() == 0){
            throw new NoContentFoundException();
        }else{
            return chargingPortBookingsList;
        }
    }


    public ChargingPortBooking getBookingById(long id) throws Exception {
        return chargingPortBookingRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    public List<ChargingPortBookingByUserDto> getBookingsByUserId(long uid) throws Exception {

        User user = userRepository.findById(uid).orElseThrow(() -> new NotFoundException());
        List<ChargingPortBooking> chargingPortBookingList = chargingPortBookingRepository.findByUserId(uid);
        if (chargingPortBookingList.size() == 0) {
            throw new NoContentFoundException();
        } else {
            return getBookingDetails(chargingPortBookingList);
        }

    }

    private List<ChargingPortBookingByUserDto> getBookingDetails(List<ChargingPortBooking> chargingPortBookingList) {
        List<ChargingPortBookingByUserDto> chargingPortBookingByUserDtos = chargingPortBookingList.stream().map(chargingPortBooking -> {
            return chargingPortBookingMapper.toDto(chargingPortBooking);
        }).toList();

        return chargingPortBookingByUserDtos.stream().map(
                chargingPortBookingByUserDto -> {
                    chargingPortBookingByUserDto.getChargingPort().setFloorName(chargingPortBookingList.get(0).getChargingPort().getFloor().getFloorName());
                    chargingPortBookingByUserDto.getChargingPort().setWing(chargingPortBookingList.get(0).getChargingPort().getFloor().getWing());
                    //Calculating the duration of Reservation
                    LocalDateTime start = chargingPortBookingByUserDto.getReservationStartTime();
                    LocalDateTime end = chargingPortBookingByUserDto.getReservationEndTime();
                    Duration duration = Duration.between(end, start);
                    duration = duration.abs();
                    chargingPortBookingByUserDto.setReservedDuration(duration.toHours()+"hrs");

                    return chargingPortBookingByUserDto;
                }).toList();

    }

    public List<ChargingPortBookingByUserDto> getBookingsByChargingPortId(long cpid) throws Exception {
        chargingPortRepository.findById(cpid).orElseThrow(() ->  new NotFoundException());
        List<ChargingPortBooking> chargingPortBookingList = chargingPortBookingRepository.findByChargingPortId(cpid);

        if(chargingPortBookingList.size() == 0){
            throw new NoContentFoundException();
        }else {
            return getBookingDetails(chargingPortBookingList);
        }
    }

    public ChargingPortBookingResponseDto bookChargingPort(ChargingPortBookingRequestDto bookingRequestDto) throws Exception{

        User user = userRepository.findById(bookingRequestDto.getUserId()).orElseThrow(() -> new NotFoundException("No such user exist"));

        if(entryLogStatusChecker.getLibraryEntryStatus(user.getId())){

            ChargingPort chargingPort = chargingPortRepository.findById(bookingRequestDto.getChargingPortId()).orElseThrow(() -> new NotFoundException("No such charging port exist"));

            if(entryLogStatusChecker.getFloorEntryStatus(user.getId(), chargingPort.getFloor().getId())){

                if(chargingPortBookingRepository.existsByUserIdAndStatus(user.getId(), BookingStatusEnum.BOOKED)){
                    throw new HasActiveBookingException("Charging Port");
                }
                //finding the ids of the respective floors
                List<Long> floorIds = floorRepository.findByFloorName(chargingPort.getFloor().getFloorName()).stream().map(floor -> {
                    return floor.getId();
                }).toList();

                //checking if there are any vacant Charging Port *****in that floor of the requested resource****
                List<ChargingPortBooking> chargingPortBookingList = chargingPortBookingRepository.findByStatus(BookingStatusEnum.BOOKED);

                //filtering the chargingPortList to match the floor to find the exact occupation of the chargingPorts
                chargingPortBookingList = chargingPortBookingList.stream()
                        .filter(chargingPortBooking -> {
                            long floorId = chargingPortBooking.getChargingPort().getFloor().getId();
                            return floorIds.contains(floorId);
                        })
                        .toList();

                long totalCpinFloor = chargingPortRepository.findByFloorIdIn(floorIds).size();
                long NoOfBookedPortsInFloor = chargingPortBookingList.size();

                if(NoOfBookedPortsInFloor >= totalCpinFloor){
                    String floorName = chargingPort.getFloor().getFloorName();
                    chargingPortWaitingListService.addUsertoWL(user, floorName);

                    //find the no of ports floor wise and then send it as the parameter
                    long noOfWaitingList = chargingPortWaitingListRepository
                            .findCountByFloorPreferenceAndWaitingListStatus(floorName, WaitingListEnum.WAITLISTED.name());


                    dynamicTimeAllocationService.changeReservationTime(noOfWaitingList, totalCpinFloor, floorIds);
                    throw new AllResourcesBookedException("Charging Port");
                }
                else if(chargingPort.getBookingStatus().equals(BookingStatusEnum.BOOKED.toString())){
                    throw new ResourceAlreadyBooked(chargingPort.getId(), "Charging Port");
                }

                boolean locked = false;
                if(!redisResourceLock.isLocked("cport",chargingPort.getId())){
                    locked = redisResourceLock.tryLock("cport", chargingPort.getId(), user.getId()+"",Duration.of(1, ChronoUnit.MINUTES));
                }

                if(locked){
                    ChargingPortBooking chargingPortBooking = new ChargingPortBooking();
                    chargingPortBooking.setChargingPort(chargingPort);
                    chargingPortBooking.setUser(user);
                    chargingPortBooking.setStatus(BookingStatusEnum.BOOKED);
                    chargingPortBooking.setReservationStartTime(LocalDateTime.now());
                    chargingPortBooking.setReservationEndTime(LocalDateTime.now().plusMinutes((long) getDuration(chargingPort.getFloor().getFloorName(), "generalPurpose")));
                    chargingPort.setStatus(BookingStatusEnum.BOOKED);
                    chargingPortRepository.save(chargingPort);
                    chargingPortBookingRepository.save(chargingPortBooking);

                    redisResourceLock.releaseLock("cport", chargingPort.getId(), user.getId()+"");

                    ChargingPortBookingResponseDto chargingPortBookingResponseDto = chargingPortBookingMapper.toResponseDto(chargingPortBooking);
                    chargingPortBookingResponseDto.setUserId(chargingPortBooking.getUser().getId());
                    chargingPortBookingResponseDto.setChargingPortId(chargingPortBooking.getChargingPort().getId());
                    chargingPortBookingResponseDto.getChargingPort().setStatus(BookingStatusEnum.BOOKED);
                    chargingPortBookingResponseDto.getChargingPort().setFloorName(chargingPortBooking.getChargingPort().getFloor().getFloorName());
                    chargingPortBookingResponseDto.getChargingPort().setWing(chargingPortBooking.getChargingPort().getFloor().getWing());
                    //notificationService.sendCPBookingNotification(chargingPortBooking);
                    return chargingPortBookingResponseDto;
                }
                else{
                    throw new ResourceAlreadyBooked(chargingPort.getId(), "Charging Port");
                }


            } else{
                throw new InvalidInput("You must have a valid Floor entry to book resources");
            }
        } else{
            throw new InvalidInput("You must have a valid Library entry to book resources of library");
        }
    }


    @Transactional
    public ChargingPortBookingByUserDto clearChargingPortBooking(ClearChargingPortBookingDto clearChargingPortBookingDto) throws Exception {


        ChargingPortBooking chargingPortBooking = chargingPortBookingRepository.findByUserIdAndStatus(
                clearChargingPortBookingDto.getUserId(), BookingStatusEnum.BOOKED);

            if(chargingPortBooking != null){


                chargingPortBooking.setStatus(BookingStatusEnum.CLEARED);
                chargingPortBooking.setReservationEndTime(LocalDateTime.now());
                ChargingPort chargingPort = chargingPortBooking.getChargingPort();
                chargingPort.setStatus(BookingStatusEnum.AVAILABLE);
                chargingPortRepository.save(chargingPort);
                chargingPortBookingRepository.save(chargingPortBooking);
                String floorName = chargingPort.getFloor().getFloorName();

                //now check for any waitListed User
                try {
                    //allocating the user a Charging Port
                    //***********change the getduration method to calculate the floor wise duration by sending the
                    //************** waiting list size specific to the floorString
                    chargingPortWaitingListService.allocateChargingPort(chargingPort, getDuration(floorName, "increaseTime"));
                } catch (Exception e){
                    e.printStackTrace();
                }

                //adjust time accordingly
                adjustDuration(floorName);
            } else{
                throw new NoActiveBookingException("Charging Port");
            }

            List<ChargingPortBooking> chargingPortBookingList = new ArrayList<>();
            chargingPortBookingList.add(chargingPortBooking);
            //notificationService.sendCPClearingNotification(chargingPortBooking);
            return getBookingDetails(chargingPortBookingList).get(0);
    }


    public void deleteBookingsBeforeDate(LocalDateTime localDateTime) {
        chargingPortBookingRepository.deleteByReservationStartTimeBefore(localDateTime);
    }

    public List<ChargingPortBooking> getChargingPortBookingByStatus(BookingStatusEnum status){
        return chargingPortBookingRepository.findByStatus(status);
    }

    public double getDuration(String floorName, String purpose) {
        List<Long> floorIds = floorRepository.findByFloorName(floorName).stream().map(floor -> {
            return floor.getId();
        }).toList();
        long totalCpinFloor = chargingPortRepository.findByFloorIdIn(floorIds).size();
        long waitingListSize = chargingPortWaitingListService.getActiveWLSizeByFloorIds( WaitingListEnum.WAITLISTED, floorName);
        double durationOfBooking = 0;
        if(purpose.equals("generalPurpose"))
            durationOfBooking = dynamicTimeAllocationService.getDynamicAllocationTimeForCp(waitingListSize, totalCpinFloor);
        else if(purpose.equals("increaseTime"))
         durationOfBooking = dynamicTimeAllocationService.getDynamicAllocationTimeForCp(waitingListSize-1, totalCpinFloor);

        return durationOfBooking;
    }

    @Transactional
    public void adjustDuration(String floorName){
        List<Long> floorIds = floorRepository.findByFloorName(floorName).stream().map(floor -> {
            return floor.getId();
        }).toList();
        dynamicTimeAllocationService.updateReservationTime((long)getDuration(floorName, "generalPurpose"), floorIds);
    }
}
