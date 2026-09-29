package com.socialmedia.dto;


import lombok.Data;

@Data
public class FriendRequestDto {

    private Long connectionId;
    private String requesterEmail;
    private String receiverEmail;
    private String status;
}
