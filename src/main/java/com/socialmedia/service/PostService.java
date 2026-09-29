package com.socialmedia.service;

import com.socialmedia.dto.CommentResponseDto;
import com.socialmedia.dto.PostResponseDto;
import com.socialmedia.entity.Comment;
import com.socialmedia.entity.FriendConnection;
import com.socialmedia.entity.Post;
import com.socialmedia.entity.PostLike;
import com.socialmedia.entity.User;
import com.socialmedia.repository.CommentRepository;
import com.socialmedia.repository.FriendConnectionRepository;
import com.socialmedia.repository.PostLikeRepository;
import com.socialmedia.repository.PostRepository;
import com.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendConnectionRepository friendConnectionRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private CommentRepository commentRepository;

    public PostResponseDto createPost(String email,String content)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        Post savedPost=postRepository.save(post);

        return mapToDto(savedPost);
    }

   public List<PostResponseDto> getFeedPosts(String email)
   {
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        List<User> network = new ArrayList<>();
        network.add(currentUser);

        List<FriendConnection> connections= friendConnectionRepository.findAcceptedConnections(currentUser);

        for(FriendConnection conn:connections)
        {
            if(conn.getRequester().getId().equals(currentUser.getId()))
            {
                network.add(conn.getReceiver());
            }
            else
            {
                network.add(conn.getRequester());
            }
        }

       List<Post> posts = postRepository.findByUserInOrderByCreatedAtDesc(network);

       return posts.stream().map(this::mapToDto).collect(Collectors.toList());
   }

    public void likePost(String email, Long postId) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));

        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);
        if (existingLike.isEmpty()) {
            PostLike like = new PostLike();
            like.setPost(post);
            like.setUser(user);
            postLikeRepository.save(like);
        }
    }

    public void unlikePost(String email, Long postId) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));

        postLikeRepository.findByPostAndUser(post, user).ifPresent(postLikeRepository::delete);
    }

    public CommentResponseDto addComment(String email, Long postId, String content) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(()->new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        Comment savedComment = commentRepository.save(comment);

        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(savedComment.getId());
        dto.setContent(savedComment.getContent());
        dto.setUsername(savedComment.getUser().getUsername());
        dto.setCreatedAt(savedComment.getCreatedAt());
        return dto;
    }

    public List<CommentResponseDto> getComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
        return comments.stream().map(comment -> {
            CommentResponseDto dto = new CommentResponseDto();
            dto.setId(comment.getId());
            dto.setContent(comment.getContent());
            dto.setUsername(comment.getUser().getUsername());
            dto.setCreatedAt(comment.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public PostResponseDto mapToDto(Post post)
    {
            PostResponseDto dto=new PostResponseDto();

            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setUsername(post.getUser().getUsername());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setLikeCount(postLikeRepository.countByPostId(post.getId()));
            dto.setCommentCount(commentRepository.countByPostId(post.getId()));
            return dto;
    }
}
