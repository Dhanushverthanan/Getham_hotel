package com.getham.hotel.repository;

import com.getham.hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.roomId FROM Booking b WHERE b.status != 'CANCELLED' AND (b.checkIn < :checkOut AND b.checkOut > :checkIn))")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}
