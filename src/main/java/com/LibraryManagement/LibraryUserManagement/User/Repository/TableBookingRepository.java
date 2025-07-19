package com.LibraryManagement.LibraryUserManagement.User.Repository;

import com.LibraryManagement.LibraryUserManagement.User.Entities.TableBooking;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface TableBookingRepository extends JpaRepository<TableBooking, Long> {

    List<TableBooking> findByTablesId(Long tableId);
    List<TableBooking> findByUserId(Long userId);
    boolean existsByTablesIdAndStatus(Long tableId, BookingStatusEnum status);
    boolean existsByUserIdAndStatus(Long userId, BookingStatusEnum status);
    TableBooking findByUserIdAndStatus(Long userId, BookingStatusEnum status);
    void deleteByReservationStartTimeBefore(LocalDateTime localDateTime);
    List<TableBooking> findByStatus(BookingStatusEnum statusEnum);


}
