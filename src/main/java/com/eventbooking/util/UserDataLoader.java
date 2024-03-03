package com.eventbooking.util;

import com.eventbooking.entity.User;
import com.eventbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create and save four user objects
        userRepository.save(User.builder()
                .name("test1")
                .email("test1@email.com")
                .password(passwordEncoder.encode("password"))
                .build());
        userRepository.save(User.builder()
                .name("test2")
                .email("test2@email.com")
                .password(passwordEncoder.encode("password"))
                .build());
        userRepository.save(User.builder()
                .name("test3")
                .email("test3@email.com")
                .password(passwordEncoder.encode("password"))
                .build());

    }
}
