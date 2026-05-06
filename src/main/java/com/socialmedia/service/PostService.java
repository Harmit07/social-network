package com.socialmedia.service;


import com.socialmedia.dto.PostResponseDto;
import com.socialmedia.entity.FriendConnection;
import com.socialmedia.entity.Post;
import com.socialmedia.entity.User;
import com.socialmedia.repository.FriendConnectionRepository;
import com.socialmedia.repository.PostRepository;
import com.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendConnectionRepository friendConnectionRepository;

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
    public PostResponseDto mapToDto(Post post)
    {
            PostResponseDto dto=new PostResponseDto();

            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setUsername(post.getUser().getUsername());
            dto.setCreatedAt(post.getCreatedAt());
            return dto;
    }

}

