package com.getham.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.getham.hotel.entity.Room;
import com.getham.hotel.repository.RoomRepository;
import com.getham.hotel.entity.User;
import com.getham.hotel.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GethamHotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(GethamHotelApplication.class, args);
        System.out.println("Application started successfully");
    }

    @Bean
    public CommandLineRunner initData(RoomRepository roomRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole("ADMIN");
                admin.setEmail("admin@getham.com");
                admin.setPhone("0000000000");
                userRepository.save(admin);
                System.out.println("Default ADMIN account initialized: admin / admin");
            }

            if (roomRepository.count() == 0) {
                Room r1 = new Room();
                r1.setNumber("101");
                r1.setType("Deluxe");
                r1.setPrice(150.0);
                r1.setAvailable(true);
                roomRepository.save(r1);

                Room r2 = new Room();
                r2.setNumber("102");
                r2.setType("Suite");
                r2.setPrice(300.0);
                r2.setAvailable(true);
                roomRepository.save(r2);

                Room r3 = new Room();
                r3.setNumber("201");
                r3.setType("Villa");
                r3.setPrice(500.0);
                r3.setAvailable(true);
                roomRepository.save(r3);
                
                System.out.println("Sample rooms initialized in the database!");
            }
        };
    }

}
