package com.vestaChrono.linkedin.post_service.services;

import com.vestaChrono.linkedin.post_service.auth.UserContextHolder;
import com.vestaChrono.linkedin.post_service.entity.Post;
import com.vestaChrono.linkedin.post_service.entity.PostLike;
import com.vestaChrono.linkedin.post_service.event.PostLikedEvent;
import com.vestaChrono.linkedin.post_service.exception.BadRequestException;
import com.vestaChrono.linkedin.post_service.exception.ResourceNotFoundException;
import com.vestaChrono.linkedin.post_service.repository.PostLikeRepository;
import com.vestaChrono.linkedin.post_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postRepository;

    public void likePost(Long postId) {
//        get the userId from the UserContextHolder
        Long userId = UserContextHolder.getCurrentUserid();
        log.info("Attempting to like the post with id: {}", postId);

        //    check if the post exists in the repository
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with Id: " + postId));

//        check if the user has already liked the post
        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyLiked) throw new BadRequestException("Cannot like the same post again");

//        create a new postLike object and Like the post if not already liked
        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        log.info("Post with id: {} liked successfully", postId);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .creatorId(post.getUserId())
                .likedByUserId(userId)
                .build();

        kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
    }

    public void unlikePost(Long postId) {
//        get the userId from the UserContextHolder
        Long userId = UserContextHolder.getCurrentUserid();
//        check if the post exists in the repository
        log.info("Attempting to unlike the post with id: {}", postId);
        boolean exists = postRepository.existsById(postId);
        if (!exists) throw new ResourceNotFoundException("Post not found with Id: " + postId);

//        check if user has liked the post or not
        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (!alreadyLiked) throw new BadRequestException("User has to like the post to unlike");

//        unlike the post if already liked
        postLikeRepository.deleteByUserIdAndPostId(userId, postId);

        log.info("Post with id: {} unliked successfully", postId);
    }
}
