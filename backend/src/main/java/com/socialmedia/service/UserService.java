package com.socialmedia.service;

import com.socialmedia.dto.UserRegistrationDto;
import com.socialmedia.entity.User;
import com.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDto dto)
    {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

    public com.socialmedia.dto.UserProfileDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToProfileDto(user);
    }

    public com.socialmedia.dto.UserProfileDto getUserProfileByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToProfileDto(user);
    }

    public com.socialmedia.dto.UserProfileDto updateUserProfile(String email, com.socialmedia.dto.UserProfileDto dto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setBio(dto.getBio());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());
        userRepository.save(user);
        return mapToProfileDto(user);
    }

    private com.socialmedia.dto.UserProfileDto mapToProfileDto(User user) {
        com.socialmedia.dto.UserProfileDto dto = new com.socialmedia.dto.UserProfileDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        return dto;
    }
}
