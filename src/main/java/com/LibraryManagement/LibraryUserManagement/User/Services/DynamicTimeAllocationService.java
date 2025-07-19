package com.LibraryManagement.LibraryUserManagement.User.Services;

import com.LibraryManagement.LibraryUserManagement.User.Repository.ChargingPortBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DynamicTimeAllocationService {

    @Autowired
    private ChargingPortBookingRepository chargingPortBookingRepository;

    public double getDynamicAllocationTimeForCp(long waitingListSize, long noOfPorts){

   double MAX_CHARGING_TIME = 20;
   double MIN_CHARGING_TIME = 5;

   double maxAllowedWaitingList = noOfPorts;
   double time;
    if(waitingListSize > 0)
        time = MAX_CHARGING_TIME  - 1 * waitingListSize;
    else
        time = MAX_CHARGING_TIME;
   if(waitingListSize >= maxAllowedWaitingList || time < MIN_CHARGING_TIME){
       return MIN_CHARGING_TIME;
   }
    return time;
    }

    @Transactional
    public void changeReservationTime(long waitingListSize, long noOfPorts, List<Long> floorIds){
        long time = (long)getDynamicAllocationTimeForCp(waitingListSize, noOfPorts);
        chargingPortBookingRepository.updateReservationTime(time , floorIds);
    }

    public void updateReservationTime(long time, List<Long> floorIds) {
        chargingPortBookingRepository.updateReservationTime(time , floorIds);
    }
}
