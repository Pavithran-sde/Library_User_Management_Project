package com.LibraryManagement.LibraryUserManagement.User.Services;

import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ClearChargingPortBookingDto;
import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPortBooking;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChargingPortScheduler {

    @Autowired
    private ChargingPortBookingService chargingPortBookingService;

    @Scheduled(fixedRate =  2 * 60 * 1000)
    @Transactional
    public void updateChargingPortBookingStatusViaScheduler() {
        List<ChargingPortBooking> list = chargingPortBookingService.getChargingPortBookingByStatus(BookingStatusEnum.BOOKED);
        if (list.size() > 0) {
            list.stream().forEach(
                    chargingPortBooking -> {
                        if (chargingPortBooking.getReservationEndTime().isBefore(LocalDateTime.now())) {
                            try {
                                chargingPortBookingService.clearChargingPortBooking(new ClearChargingPortBookingDto(chargingPortBooking.getUser().getId()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
