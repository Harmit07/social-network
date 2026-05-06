package com.socialmedia.controller;

import com.socialmedia.dto.UserLoginDto;
import com.socialmedia.dto.UserRegistrationDto;
import com.socialmedia.entity.User;
import com.socialmedia.repository.UserRepository;
import com.socialmedia.security.JwtUtil;
import com.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController //convert user object into json data
    @RequestMapping("/api/users")//map to address
    @CrossOrigin(origins="*")//any localhost can listen to any server

    public class UserController{
        @Autowired
        private UserService userService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtUtil jwtUtil;

        @PostMapping("/register")//trigger post method(data come from website)
        public ResponseEntity<User>registerUser(@RequestBody UserRegistrationDto registrationDto)//convert it into dto automatically(request body)
        {
            User savedUser= userService.registerUser(registrationDto);
            return ResponseEntity.ok(savedUser);

        }

        @PostMapping("/login")
        public ResponseEntity<String> loginUser(@RequestBody UserLoginDto loginDto){
            Optional<User> userOptional=userRepository.findByEmail(loginDto.getEmail());

            if (userOptional.isPresent())
            {
                User user = userOptional.get();

                if(passwordEncoder.matches(loginDto.getPassword(),user.getPasswordHash()))
                {
                    String token= jwtUtil.generateToken(user.getEmail());
                    return ResponseEntity.ok(token);
                }
            }
            return ResponseEntity.status(401).body("Invalid email or password");
        }

    }


