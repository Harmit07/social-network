package com.socialmedia.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="posts")
@Data
@NoArgsConstructor

public class Post {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @ManyToOne(fetch=FetchType.LAZY)
 @JoinColumn(name="user_id",nullable = false)
    private User user;

 @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

 @Column(name="media_url")
    private String mediaUrl;

 @CreationTimestamp
    @Column(name="created_at",updatable = false)
    private LocalDateTime createdAt;


}
