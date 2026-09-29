package com.socialmedia.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private String username;
    private String email;
    private String bio;
    private String profilePictureUrl;
}
