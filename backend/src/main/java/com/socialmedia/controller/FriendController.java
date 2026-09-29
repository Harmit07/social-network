package com.socialmedia.controller;

import com.socialmedia.dto.FriendRequestDto;
import com.socialmedia.dto.SendFriendRequestDto;
import com.socialmedia.dto.UserProfileDto;
import com.socialmedia.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "*")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<String> sendRequest(@RequestBody SendFriendRequestDto payload, Principal principal) {
        String response = friendService.sendFriendRequest(principal.getName(), payload.getReceiverEmail());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/accept/{connectionId}")
    public ResponseEntity<String> acceptRequest(@PathVariable Long connectionId, Principal principal) {
        String response = friendService.acceptRequest(connectionId, principal.getName());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/reject/{connectionId}")
    public ResponseEntity<String> rejectRequest(@PathVariable Long connectionId, Principal principal) {
        return ResponseEntity.ok(friendService.rejectRequest(connectionId, principal.getName()));
    }

    @DeleteMapping("/remove/{connectionId}")
    public ResponseEntity<String> removeFriend(@PathVariable Long connectionId, Principal principal) {
        return ResponseEntity.ok(friendService.removeFriend(connectionId, principal.getName()));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestDto>> getPendingRequests(Principal principal) {
        return ResponseEntity.ok(friendService.getPendingRequests(principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto>> getFriends(Principal principal) {
        return ResponseEntity.ok(friendService.getFriends(principal.getName()));
    }
}
