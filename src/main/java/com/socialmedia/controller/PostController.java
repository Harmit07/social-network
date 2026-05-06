package com.socialmedia.controller;


import com.socialmedia.dto.PostResponseDto;
import com.socialmedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins="http://localhost:5173")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto>createPost(@RequestBody Map<String ,String>payload, Principal principal)
    {
        PostResponseDto newPost=postService.createPost(principal.getName(),payload.get("content"));
        return ResponseEntity.ok(newPost);
    }
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(Principal principal) {
        // Pass the VIP badge email into our new algorithm!
        return ResponseEntity.ok(postService.getFeedPosts(principal.getName()));
    }
}
