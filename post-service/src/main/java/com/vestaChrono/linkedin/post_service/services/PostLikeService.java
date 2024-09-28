package com.vestaChrono.linkedin.post_service.services;

import com.vestaChrono.linkedin.post_service.entity.PostLike;
import com.vestaChrono.linkedin.post_service.exception.BadRequestException;
import com.vestaChrono.linkedin.post_service.exception.ResourceNotFoundException;
import com.vestaChrono.linkedin.post_service.repository.PostLikeRepository;
import com.vestaChrono.linkedin.post_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postRepository;

    public void likePost(Long postId, Long userId) {
//    check if the post exists in the repository
        log.info("Attempting to like the post with id: {}", postId);
        boolean exists = postRepository.existsById(userId);
        if (!exists) throw new ResourceNotFoundException("Post not found with Id: " + postId);

//        check if the user has already liked the post
        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyLiked) throw new BadRequestException("Cannot like the same post again");

//        Like the post if not already liked
        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        log.info("Post with id: {} liked successfully", postId);
    }
}
