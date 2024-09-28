package com.vestaChrono.linkedin.post_service.services;

import com.vestaChrono.linkedin.post_service.dto.PostCreateRequestDto;
import com.vestaChrono.linkedin.post_service.dto.PostDto;
import com.vestaChrono.linkedin.post_service.entity.Post;
import com.vestaChrono.linkedin.post_service.exception.ResourceNotFoundException;
import com.vestaChrono.linkedin.post_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostsRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDto createPost(PostCreateRequestDto postDto, Long userId) {
//        convert to Post entity
        Post post = modelMapper.map(postDto, Post.class);
//        set the userId to the post
        post.setUserId(userId);

//        save the post in repository
        Post savedPost = postRepository.save(post);
//        return it as a Dto
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with Id: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        return modelMapper.map(post, PostDto.class);
    }
}
