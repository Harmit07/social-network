package com.socialmedia.repository;

import com.socialmedia.entity.Post;
import com.socialmedia.entity.PostLike;
import com.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
    long countByPostId(Long postId);
}
