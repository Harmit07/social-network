package com.socialmedia.controller;

import com.socialmedia.dto.UserLoginDto;
import com.socialmedia.dto.UserRegistrationDto;
import com.socialmedia.dto.UserProfileDto;
import com.socialmedia.entity.User;
import com.socialmedia.repository.UserRepository;
import com.socialmedia.security.JwtUtil;
import com.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;


@RestController 
@RequestMapping("/api/users")
@CrossOrigin(origins="*")
public class UserController{
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User>registerUser(@RequestBody UserRegistrationDto registrationDto)
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

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(Principal principal) {
        return ResponseEntity.ok(userService.getUserProfile(principal.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateProfile(@RequestBody UserProfileDto profileDto, Principal principal) {
        return ResponseEntity.ok(userService.updateUserProfile(principal.getName(), profileDto));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserProfileByUsername(username));
    }
}
