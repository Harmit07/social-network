package com.socialmedia.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDto {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;

}
