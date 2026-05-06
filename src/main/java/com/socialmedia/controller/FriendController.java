package com.socialmedia.controller;


import com.socialmedia.dto.FriendRequestDto;
import com.socialmedia.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.socialmedia.dto.SendFriendRequestDto;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/friends")
@CrossOrigin(origins = "http://localhost:5173")
public class FriendController {


    @Autowired
    private FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<String> sendRequest(@RequestBody SendFriendRequestDto payload, Principal principal) {

        // Notice we now use payload.getReceiverEmail() instead of payload.get("...")
        String response = friendService.sendFriendRequest(principal.getName(), payload.getReceiverEmail());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/accept/{connectionId}")
    public ResponseEntity<String> acceptRequest(@PathVariable Long connectionId,Principal principal)
    {
        String response = friendService.acceptRequest(connectionId,principal.getName());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/pending")
    public ResponseEntity<List<FriendRequestDto>> getPendingRequests(Principal principal)
    {
        return ResponseEntity.ok(friendService.getPendingRequests(principal.getName()));

    }
}
