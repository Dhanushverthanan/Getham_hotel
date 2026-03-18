package com.getham.hotel.repository;

import com.getham.hotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    long countByStatus(String status);

    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.status = 'COMPLETED'")
    Double sumTotalRevenue();

    @Query("SELECT MONTH(b.checkIn) as month, SUM(b.totalPrice) as revenue FROM Booking b WHERE b.status = 'COMPLETED' GROUP BY MONTH(b.checkIn)")
    List<Object[]> getRevenueByMonth();

    List<Booking> findByRoomId(Long roomId);
}
