package com.LibraryManagement.LibraryUserManagement.User.Services;

import com.LibraryManagement.LibraryUserManagement.Admin.Repository.FloorRepository;
import com.LibraryManagement.LibraryUserManagement.Common.Utilities.Services.EntryLogStatusChecker;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.ClearTableBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.TableBookingByUserDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.TableBookingRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.TableBookingResponseDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.TableBooking;
import com.LibraryManagement.LibraryUserManagement.User.Entities.Tables;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import com.LibraryManagement.LibraryUserManagement.User.Enum.UserFloorPreferenceEnum;
import com.LibraryManagement.LibraryUserManagement.User.Exceptions.*;
import com.LibraryManagement.LibraryUserManagement.User.Mappers.TableBookingMapper;
import com.LibraryManagement.LibraryUserManagement.Common.Redis.Resources.RedisResourceLock;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableBookingRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.TableRepository;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TableBookingService {

    @Autowired
    private TableBookingRepository tableBookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private TableBookingMapper tableBookingMapper;
    @Autowired
    private TableWaitingListService tableWaitingListService;
    @Autowired
    private RedisResourceLock redisResourceLock;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private EntryLogStatusChecker entryLogStatusChecker;



    public List<User> getAllUsersHavingBookingToday(){
        List<TableBooking> userIds = tableBookingRepository.findByStatus(BookingStatusEnum.BOOKED);
        return userIds.stream().map(tableBooking -> {
            return tableBooking.getUser();
        }).toList();
    }


    public List<TableBooking> getAllBookings() throws NoContentFoundException, Exception{
       List<TableBooking> tableBookingList = tableBookingRepository.findAll();
        if(tableBookingList.size() == 0){
            throw new NoContentFoundException();
        }else{
            return tableBookingList;
        }
    }

    public TableBooking getBookingById(long id) throws NotFoundException, Exception {
        return tableBookingRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }


//======================== **change the tableMapper method to return Booking_tableInfoDto
// ======================= instead of TableWithBookingStatusInfoDto** ===============

    public List<TableBookingByUserDto> getBookingsByUserId(long id) throws NotFoundException, Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("No such user exist"));
        List<TableBooking> tableBookingList = tableBookingRepository.findByUserId(id);
        if (tableBookingList.size() == 0) {
            throw new NoContentFoundException();
        } else {
            return getBookingDetails(tableBookingList);
        }
    }

    public List<TableBookingByUserDto> getBookingsByTablesId(long id) throws NotFoundException, Exception {
        tableRepository.findById(id).orElseThrow(() ->  new NotFoundException("No Such Table Exist to book"));
        List<TableBooking> tableBookingList = tableBookingRepository.findByTablesId(id);

        if(tableBookingList.size() == 0){
            throw new NoContentFoundException();
        }else {
            return getBookingDetails(tableBookingList);
        }
    }

    private List<TableBookingByUserDto> getBookingDetails(List<TableBooking> tableBookingList) throws  Exception {
            List<TableBookingByUserDto> tableBookingByUserDtoList = tableBookingList.stream().map(tableBooking -> {
                return tableBookingMapper.toDto(tableBooking);
            }).toList();

            return tableBookingByUserDtoList.stream().map(
                    tableBookingByUserDto -> {
                        tableBookingByUserDto.getTables().setFloorName(tableBookingList.get(0).getTables().getFloor().getFloorName());
                        tableBookingByUserDto.getTables().setWing(tableBookingList.get(0).getTables().getFloor().getWing());
                        tableBookingByUserDto.getTables().setBookingStatusEnum(tableBookingList.get(0).getTables().getStatus());
                        //Calculating the duration of Reservation
                        LocalDateTime start = tableBookingByUserDto.getReservationStartTime();
                        LocalDateTime end = tableBookingByUserDto.getReservationEndTime();
                        Duration duration = Duration.between(end, start);
                        duration = duration.abs();
                        tableBookingByUserDto.setReservedDuration(duration.toHours()+"hrs");

                        return tableBookingByUserDto;
                    }).toList();
    }

    public String getBookingCount() {
        List<TableBooking> tableBookingList = tableBookingRepository.findByStatus(BookingStatusEnum.BOOKED);
        return "Total Bookings : "+ tableBookingList.size() + " / total tables  :"+ tableRepository.findAll().size();
    }

    @Transactional()
    public TableBookingResponseDto bookTable(TableBookingRequestDto tableBookingRequestDto) throws Exception {

        /**
         * steps to make a booking
         *  1. first check for valid userid and tableid
         *  2. check if user currently has any active booking
         *  3. check if the table that user is booking is available - 1. DB -> 2. redis lock
         *  4. if all the above are satisfied then,
         *      4.1 update the table as booked
         *      4.2 update the booking table
         *  5. clear the redis-lock
         *  6. return status to user
         */

        /**
         * 2ND ITERATION
         * Automatic Waiting List feature is being added
         1. first check for valid userid and tableid
         *  2. check if user currently has any active booking
         *  3. check if the table that user is booking is available - 1. DB -> 2. redis lock
         *  4. check if there is vacant table
         *      4.1 if yes, the
         *          4.1.1 update the table as booked
         *          4.1.2 update the booking table
         *      4.2 if no, then
         *          4.2.1 add the user to the waiting list
         *  5. clear the redis-lock
         *  6. return status to user
         */

        User user = userRepository.findById(tableBookingRequestDto.getUserId()).orElseThrow(() -> new NotFoundException("No such User found"));
        Tables table = tableRepository.findById(tableBookingRequestDto.getTablesId()).orElseThrow(() -> new NotFoundException("No such Table found"));

        if(entryLogStatusChecker.getLibraryEntryStatus(user.getId())){

            if(entryLogStatusChecker.getFloorEntryStatus(user.getId(), table.getFloor().getId())){


                if(tableBookingRepository.existsByUserIdAndStatus(user.getId(), BookingStatusEnum.BOOKED)){
                    throw new HasActiveBookingException("table");
                }
                System.out.println("Table Id : "+tableBookingRequestDto.getTablesId());
                //get the list of floor id with the floorName to get all the table associated with that floor irrespective of the wing
                List<Long> floorIds = floorRepository.findByFloorName(table.getFloor().getFloorName()).stream()
                        .map(floor -> {
                            return floor.getId();
                        }).toList();

                //checking if there are any vacant table
                List<TableBooking> tableBookingList = tableBookingRepository.findByStatus(BookingStatusEnum.BOOKED);
                //check the vacant tables in specific floor
                tableBookingList = tableBookingList.stream().filter( tableBooking -> {
                    long floorId = tableBooking.getTables().getFloor().getId();
                    return floorIds.contains(floorId);
                }).toList();

                long totalTablesInFloor = tableRepository.findByFloorIdIn(floorIds).size();
                long noOfOccupiedTables = tableBookingList.size();

                if(noOfOccupiedTables >= totalTablesInFloor){
                    if(tableBookingRequestDto.getPreference().equals(UserFloorPreferenceEnum.SAME))
                    {
                        tableWaitingListService.addUsertoWL(user, table.getFloor().getFloorName(), tableBookingRequestDto.getPreference());
                        throw new AllResourcesBookedException("tables");
                    }
                    else{
                        throw new AllResourceInFloorAreBooked("All the Table in this Floor are Booked, Kindly check in other floors");
                    }
                }
                else if(table.getBookingStatus().equals(BookingStatusEnum.BOOKED.toString())){
                    throw new ResourceAlreadyBooked(table.getId(), "table");
                }

                boolean locked = false;
                if(!redisResourceLock.isLocked("table", table.getId())){
                    locked = redisResourceLock.tryLock("table", table.getId(), user.getId()+"",Duration.of(5, ChronoUnit.MINUTES));
                }

                if(locked){
                    TableBooking tableBooking = new TableBooking();
                    tableBooking.setTables(table);
                    tableBooking.setUser(user);
                    tableBooking.setStatus(BookingStatusEnum.BOOKED);
                    tableBooking.setReservationStartTime(LocalDateTime.now());
                    tableBooking.setReservationEndTime(null);
                    table.setStatus(BookingStatusEnum.BOOKED);
                    tableRepository.save(table);
                    tableBookingRepository.save(tableBooking);

                    redisResourceLock.releaseLock("table", table.getId(), user.getId()+"");

                    TableBookingResponseDto tableBookingResponseDto = tableBookingMapper.toResponseDto(tableBooking);
                    tableBookingResponseDto.getTables().setFloorName(tableBooking.getTables().getFloor().getFloorName());
                    tableBookingResponseDto.getTables().setWing(tableBooking.getTables().getFloor().getWing());
                    tableBookingResponseDto.setUserId(tableBooking.getUser().getId());
                    tableBookingResponseDto.setTablesId(tableBooking.getTables().getId());
                    tableBookingResponseDto.getTables().setBookingStatusEnum(BookingStatusEnum.BOOKED);
                    //notificationService.sendTableBookingNotification(tableBooking);
                    return tableBookingResponseDto;
                }
                else{
                    throw new ResourceAlreadyBooked(table.getId(), "table");
                }
            }else{
                throw new InvalidInput("You need to have a valid floor entry to make a booking to the resources");
            }

        } else {
            throw new InvalidInput("You must have a valid Library entry to make booking of the resources");
        }




    }

    @Transactional
    public TableBookingByUserDto clearTableBooking(ClearTableBookingDto clearTableBookingDto) throws Exception {
        /**
         * steps to be followed to clear a booking
         * 1. check if user has any active bookings
         * 2.1 if yes then update the booking to cleared and update the reserve end time
         * 2.2 if no then send a warning(exception) to the user stating that there exist no active bookin in thier account
         *
         */

        /**
         * 2nd Iteration
         * steps to be followed to clear a booking
         * 1. check if user has any active bookings
         * 2.1 if yes then update the booking to cleared and update the reserve end time
         *     2.1.1 if tablebooking is cleared then,
         *      2.1.1.1 check for any waititng list if present then,
         *          a. get the first user from the waiting list
         *          b. allocate him the tables that just got vacant
         *          c. set his walitinglist to booked
         * 2.2 if no then send a warning(exception) to the user stating that there exist no active bookin in thier account
         *
         */

        TableBooking tableBooking = tableBookingRepository.findByUserIdAndStatus(
                clearTableBookingDto.getUserId(), BookingStatusEnum.BOOKED);

            if(tableBooking != null){
                tableBooking.setStatus(BookingStatusEnum.CLEARED);
                tableBooking.setReservationEndTime(LocalDateTime.now());
                Tables tables = tableBooking.getTables();
                tables.setStatus(BookingStatusEnum.AVAILABLE);
                tableRepository.save(tables);
                tableBookingRepository.save(tableBooking);
                //now check for any waitListed User
                try {
                    //allocating the user a table
                    tableWaitingListService.allocateTable(tables);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else{
                throw new NoActiveBookingException("table");
            }

            List<TableBooking> tableBookingList = new ArrayList<>();
            tableBookingList.add(tableBooking);
            //notificationService.sendTableClearingNotification(tableBooking);
            return getBookingDetails(tableBookingList).get(0);
        }

    @Transactional
    public void deleteBookingsBeforeDate(LocalDateTime localDateTime) {
            tableBookingRepository.deleteByReservationStartTimeBefore(localDateTime);
    }

}
