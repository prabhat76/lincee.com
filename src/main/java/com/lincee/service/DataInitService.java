package com.lincee.service;

import com.lincee.entity.User;
import com.lincee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataInitService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@lincee.com", passwordEncoder.encode("password123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin@lincee.com / password123");
        }
        
        // Create a test customer user
        if (!userRepository.existsByUsername("customer")) {
            User customer = new User("customer", "customer@example.com", passwordEncoder.encode("password123"));
            customer.setFirstName("Test");
            customer.setLastName("Customer");
            customer.setRole(User.Role.CUSTOMER);
            userRepository.save(customer);
            System.out.println("✅ Customer user created: customer@example.com / password123");
        }
    }
}