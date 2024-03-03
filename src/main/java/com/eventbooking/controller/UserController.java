package com.eventbooking.controller;

import com.eventbooking.dto.AuthRequest;
import com.eventbooking.dto.UserRequest;
import com.eventbooking.entity.User;
import com.eventbooking.service.JwtService;
import com.eventbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid UserRequest request){
        User user = userService.create(request);
        if (user != null){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

}
