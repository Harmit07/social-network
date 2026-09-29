package com.socialmedia.service;


import com.socialmedia.dto.FriendRequestDto;
import com.socialmedia.entity.ConnectionStatus;
import com.socialmedia.entity.FriendConnection;
import com.socialmedia.entity.User;
import com.socialmedia.repository.FriendConnectionRepository;
import com.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    private FriendConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    public String sendFriendRequest(String senderEmail, String receiverEmail) {



        if (receiverEmail == null || receiverEmail.trim().isEmpty()) {
            throw new RuntimeException("PAYLOAD ERROR: The receiver email was completely missing from the request sent to the server.");
        }
        if (senderEmail == null || senderEmail.trim().isEmpty()) {
            throw new RuntimeException("SECURITY ERROR: The sender email was missing from the JWT token.");
        }
        // 1. CLEAN THE DATA: Remove invisible spaces and force lowercase
        String cleanSender = senderEmail.trim().toLowerCase();
        String cleanReceiver = receiverEmail.trim().toLowerCase();

        if (cleanSender.equals(cleanReceiver)) {
            throw new RuntimeException("You cannot send a friend request to yourself.");
        }

        // 2. DETAILED ERRORS: Now we will know exactly WHICH email is causing the crash
        User sender = userRepository.findByEmail(cleanSender)
                .orElseThrow(() -> new RuntimeException("SENDER ERROR: Your token email '" + cleanSender + "' is not in the DB. Try logging out and back in."));

        User receiver = userRepository.findByEmail(cleanReceiver)
                .orElseThrow(() -> new RuntimeException("RECEIVER ERROR: Could not find a user with the exact email: '" + cleanReceiver + "'"));

        // Check if a connection already exists
        boolean alreadyExists = connectionRepository.existsByRequesterAndReceiver(sender, receiver) ||
                connectionRepository.existsByRequesterAndReceiver(receiver, sender);

        if (alreadyExists) {
            throw new RuntimeException("A connection or pending request already exists between these users.");
        }

        FriendConnection connection = new FriendConnection();
        connection.setRequester(sender);
        connection.setReceiver(receiver);
        connection.setStatus(ConnectionStatus.PENDING); // Or PENDING, based on your previous setup

        connectionRepository.save(connection);
        return "Friend request sent to " + cleanReceiver;
    }


    public String acceptRequest(Long connectionId, String receiverEmail)
    {
        FriendConnection connection=connectionRepository.findById(connectionId).orElseThrow(()-> new RuntimeException("Request not found")) ;

        if(!connection.getReceiver().getEmail().equals(receiverEmail))
        {
            throw new RuntimeException("Unauthorized to accept this request");
        }

        connection.setStatus(ConnectionStatus.ACCEPTED);
        connectionRepository.save(connection);
        return "Friend request Accepted";

    }

    public List<FriendRequestDto> getPendingRequests(String userEmail)
    {
        User user=userRepository.findByEmail(userEmail).orElseThrow();

        List<FriendConnection> pendingConnections=connectionRepository.findByReceiverAndStatus(user,ConnectionStatus.PENDING);


        System.out.println("X-RAY: Searching for " + userEmail + ". Found " + pendingConnections.size() + " pending requests in the database.");
        return pendingConnections.stream().map(conn->{
            FriendRequestDto dto= new FriendRequestDto();
            dto.setConnectionId(conn.getId());
            dto.setRequesterEmail(conn.getRequester().getEmail());
            dto.setReceiverEmail(conn.getReceiver().getEmail());
            dto.setStatus(conn.getStatus().name());
            return dto;
        }).collect(Collectors.toList());
    }

    public String rejectRequest(Long connectionId, String receiverEmail) {
        FriendConnection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!connection.getReceiver().getEmail().equals(receiverEmail)) {
            throw new RuntimeException("Unauthorized to reject this request");
        }

        connectionRepository.delete(connection);
        return "Friend request rejected";
    }

    public String removeFriend(Long connectionId, String userEmail) {
        FriendConnection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found"));

        if (!connection.getReceiver().getEmail().equals(userEmail) && !connection.getRequester().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized to remove this friend");
        }

        connectionRepository.delete(connection);
        return "Friend removed successfully";
    }

    public List<com.socialmedia.dto.UserProfileDto> getFriends(String userEmail) {
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow();
        List<FriendConnection> connections = connectionRepository.findAcceptedConnections(currentUser);

        return connections.stream().map(conn -> {
            User friend = conn.getRequester().getId().equals(currentUser.getId()) ? conn.getReceiver() : conn.getRequester();
            com.socialmedia.dto.UserProfileDto dto = new com.socialmedia.dto.UserProfileDto();
            dto.setUsername(friend.getUsername());
            dto.setEmail(friend.getEmail());
            dto.setBio(friend.getBio());
            dto.setProfilePictureUrl(friend.getProfilePictureUrl());
            return dto;
        }).collect(Collectors.toList());
    }
}
