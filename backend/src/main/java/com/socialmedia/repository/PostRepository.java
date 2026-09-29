package com.socialmedia.repository;

import com.socialmedia.entity.Post;
import com.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findByUserInOrderByCreatedAtDesc(List<User> users);

}
