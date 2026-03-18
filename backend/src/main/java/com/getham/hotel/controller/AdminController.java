package com.getham.hotel.controller;

import com.getham.hotel.entity.Booking;
import com.getham.hotel.entity.Room;
import com.getham.hotel.entity.User;
import com.getham.hotel.service.BookingService;
import com.getham.hotel.service.RoomService;
import com.getham.hotel.service.UserService;
import com.getham.hotel.repository.BookingRepository;
import com.getham.hotel.repository.RoomRepository;
import com.getham.hotel.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final BookingService bookingService;
    private final UserService userService;
    private final RoomService roomService;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public AdminController(BookingService bookingService, UserService userService, RoomService roomService,
                           BookingRepository bookingRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.roomService = roomService;
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @DeleteMapping("/booking/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/room/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        return ResponseEntity.ok(roomService.updateRoom(id, room));
    }

    @GetMapping("/stats")
    public ResponseEntity<java.util.Map<String, Object>> getStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalRooms", roomRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("activeBookings", bookingRepository.countByStatus("CONFIRMED"));
        stats.put("totalUsers", userRepository.count());
        Double revenue = bookingRepository.sumTotalRevenue();
        stats.put("totalRevenue", revenue != null ? revenue : 0.0);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/revenue")
    public ResponseEntity<List<Object[]>> getRevenue() {
        return ResponseEntity.ok(bookingRepository.getRevenueByMonth());
    }

    @GetMapping("/room/{id}/bookings")
    public ResponseEntity<List<Booking>> getRoomBookings(@PathVariable Long id) {
        return ResponseEntity.ok(bookingRepository.findByRoomId(id));
    }

    @PostMapping("/checkout/{bookingId}")
    public ResponseEntity<?> checkoutBooking(@PathVariable Long bookingId) {
        java.util.Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isPresent()) {
            Booking b = opt.get();
            b.setStatus("COMPLETED");
            bookingRepository.save(b);

            java.util.Optional<Room> rOpt = roomRepository.findById(b.getRoomId());
            if (rOpt.isPresent()) {
                Room r = rOpt.get();
                r.setAvailable(true);
                roomRepository.save(r);
            }
            return ResponseEntity.ok("Checkout successful.");
        }
        return ResponseEntity.notFound().build();
    }
}
