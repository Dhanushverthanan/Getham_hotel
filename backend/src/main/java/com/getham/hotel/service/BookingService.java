package com.getham.hotel.service;

import com.getham.hotel.entity.Booking;
import com.getham.hotel.entity.Room;
import com.getham.hotel.repository.BookingRepository;
import com.getham.hotel.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking) {
        // Mark room as unavailable
        Optional<Room> optRoom = roomRepository.findById(booking.getRoomId());
        if (optRoom.isPresent()) {
            Room r = optRoom.get();
            r.setAvailable(false);
            roomRepository.save(r);
        }
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
    }
}
