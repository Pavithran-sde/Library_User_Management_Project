package com.LibraryManagement.LibraryUserManagement.User.Repository;

import com.LibraryManagement.LibraryUserManagement.User.Entities.ChargingPortBooking;
import com.LibraryManagement.LibraryUserManagement.User.Entities.User;
import com.LibraryManagement.LibraryUserManagement.User.Enum.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@EnableJpaRepositories
public interface ChargingPortBookingRepository extends JpaRepository<ChargingPortBooking, Long> {

    List<ChargingPortBooking> findByChargingPortId(Long cportId);
    List<ChargingPortBooking> findByUserId(Long userId);
    boolean existsByChargingPortIdAndStatus(Long cportId, BookingStatusEnum status);
    boolean existsByUserIdAndStatus(Long userId, BookingStatusEnum status);
    ChargingPortBooking findByUserIdAndStatus(Long userId, BookingStatusEnum status);
    void deleteByReservationStartTimeBefore(LocalDateTime localDateTime);
    List<ChargingPortBooking> findByStatus(BookingStatusEnum statusEnum);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE charging_port_booking SET leaving_time = TIMESTAMPADD(MINUTE, :duration, booking_time) WHERE status = 'BOOKED' AND chargingport_id in (select id from charging_ports where floor_id in (:floorIds))")
    void updateReservationTime(@Param("duration") long duration,@Param("floorIds") List<Long> floorIds);
}
