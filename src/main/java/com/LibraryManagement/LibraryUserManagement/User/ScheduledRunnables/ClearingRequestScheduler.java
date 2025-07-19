package com.LibraryManagement.LibraryUserManagement.User.ScheduledRunnables;

import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ClearChargingPortBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.ClearTableBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Repository.UserRepository;
import com.LibraryManagement.LibraryUserManagement.User.Services.ChargingPortBookingService;
import com.LibraryManagement.LibraryUserManagement.User.Services.NotificationService;
import com.LibraryManagement.LibraryUserManagement.User.Services.TableBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ClearingRequestScheduler {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ChargingPortBookingService chargingPortBookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableBookingService tableBookingService;


    @Scheduled(cron = "0 45 19 * * *") // Runs at 7:15 PM every day
    public void clearBooking(){
        List<User> tableBookingUserIds = tableBookingService.getAllUsersHavingBookingToday();
        List<User> chargingPortBookingUserIds = chargingPortBookingService.getAllUsersHavingBookingForToday();

        HashSet<User> users = new HashSet<>();
        users.addAll(tableBookingUserIds);
        users.addAll(chargingPortBookingUserIds);

        for (User user : users) {
            try{
                notificationService.clearBookingNotificationForToday(user);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Scheduled(cron = "0 05 21 * * *") // Runs at 9:05 PM every day
    public void clearBookingRequestNotification(){
        List<User> tableBookingUser = tableBookingService.getAllUsersHavingBookingToday();
        List<User> chargingPortBookingUser = chargingPortBookingService.getAllUsersHavingBookingForToday();


        for (User tb : tableBookingUser) {
            try{
                tableBookingService.clearTableBooking(new ClearTableBookingDto(tb.getId()));
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        for (User cp : chargingPortBookingUser) {
            try{
                chargingPortBookingService.clearChargingPortBooking(new ClearChargingPortBookingDto(cp.getId()));
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }



}
