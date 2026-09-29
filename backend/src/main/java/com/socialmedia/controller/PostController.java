package com.socialmedia.controller;

import com.socialmedia.dto.CommentResponseDto;
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
        return ResponseEntity.ok(postService.getFeedPosts(principal.getName()));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, Principal principal) {
        postService.likePost(principal.getName(), postId);
        return ResponseEntity.ok("Post liked successfully");
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(@PathVariable Long postId, Principal principal) {
        postService.unlikePost(principal.getName(), postId);
        return ResponseEntity.ok("Post unliked successfully");
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long postId, @RequestBody Map<String, String> payload, Principal principal) {
        CommentResponseDto comment = postService.addComment(principal.getName(), postId, payload.get("content"));
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getComments(postId));
    }
}
