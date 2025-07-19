package com.LibraryManagement.LibraryUserManagement.CustomTests;

import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ChargingPortBookingRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.Services.ChargingPortBookingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChargingPortBookingConcurrencyTest {

    @Autowired
    private ChargingPortBookingService chargingPortBookingService;

    private final int NUM_OF_THREADS = 10;
    @Test
    public void testTableConcurrencyBooking() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    ChargingPortBookingRequestDto dto = new ChargingPortBookingRequestDto(finalI, 2, 12.9859584, 80.1406976);

                    try {
                        chargingPortBookingService.bookChargingPort(dto);
                        System.out.println("Cp Booking SUCCESS for USER_" + dto.getUserId());
                    } catch (Exception ex) {
                        System.out.println("Cp Booking FAILED for USER_" + dto.getUserId() + ": " + ex.getMessage());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }


        latch.await(); // Wait for all threads to complete
        executorService.shutdown();
    }

}


