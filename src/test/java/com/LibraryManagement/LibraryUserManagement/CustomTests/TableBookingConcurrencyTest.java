package com.LibraryManagement.LibraryUserManagement.CustomTests;


import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.TableBookingRequestDto;
import com.LibraryManagement.LibraryUserManagement.User.Enum.UserFloorPreferenceEnum;
import com.LibraryManagement.LibraryUserManagement.User.Services.TableBookingService;
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
public class TableBookingConcurrencyTest {

    @Autowired
    private TableBookingService tableBookingService;

    private final int NUM_OF_THREADS = 10;

    @Test
    public void testTableConcurrencyBooking() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_OF_THREADS);

        for (int i = 0; i < NUM_OF_THREADS; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    TableBookingRequestDto dto = new TableBookingRequestDto(finalI, 14, UserFloorPreferenceEnum.SAME, 12.9859584, 80.1406976);

                    try {
                        tableBookingService.bookTable(dto);
                        System.out.println("Table Booking SUCCESS for USER_" + dto.getUserId());
                    } catch (Exception ex) {
                        System.out.println("Table Booking FAILED for USER_" + dto.getUserId() + ": " + ex.getMessage());
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




