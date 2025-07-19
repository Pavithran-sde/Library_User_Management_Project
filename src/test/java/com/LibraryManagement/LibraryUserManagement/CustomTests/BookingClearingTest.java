//package com.LibraryManagement.LibraryUserManagement.CustomTests;
//
//import com.LibraryManagement.LibraryUserManagement.User.DTO.ChargingPortBookingDto.ClearChargingPortBookingDto;
//import com.LibraryManagement.LibraryUserManagement.User.DTO.TableBookingDto.ClearTableBookingDto;
//import com.LibraryManagement.LibraryUserManagement.User.Services.ChargingPortBookingService;
//import com.LibraryManagement.LibraryUserManagement.User.Services.TableBookingService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class BookingClearingTest {
//
//    @Autowired
//    private ChargingPortBookingService chargingPortBookingService;
//    @Autowired
//    private TableBookingService  tableBookingService;
//
//    private final int NUM_OF_THREADS = 10;
//
//    @Test
//    public void testTableConcurrencyBooking() throws Exception {
//        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
//        CountDownLatch latch = new CountDownLatch(NUM_OF_THREADS);
//
//        for (int i = 0; i < NUM_OF_THREADS; i++) {
//            int finalI = i;
//            executorService.submit(() -> {
//                try {
//                    //ClearChargingPortBookingDto  dto= new ClearChargingPortBookingDto(finalI);
//                    ClearTableBookingDto tdto = new ClearTableBookingDto(finalI);
//                    try {
//                        tableBookingService.clearTableBooking(tdto);
//                        System.out.println("Clearing SUCCESS for USER_" + tdto.getUserId());
//                    } catch (Exception ex) {
//                        System.out.println("Clearing FAILED for USER_" + tdto.getUserId() + ": " + ex.getMessage());
//                    }
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//
//        latch.await(); // Wait for all threads to complete
//        executorService.shutdown();
//    }
//
//}
