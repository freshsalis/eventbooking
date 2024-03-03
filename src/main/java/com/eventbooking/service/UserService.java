package com.eventbooking.service;

import com.eventbooking.dto.UserRequest;
import com.eventbooking.entity.User;
import com.eventbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User create(UserRequest request){
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(hashedPassword)
                .build();

        return userRepository.save(user);
    }
}
