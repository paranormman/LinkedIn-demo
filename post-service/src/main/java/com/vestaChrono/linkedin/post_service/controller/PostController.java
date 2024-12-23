package com.vestaChrono.linkedin.post_service.controller;

import com.vestaChrono.linkedin.post_service.PostServiceApplication;
import com.vestaChrono.linkedin.post_service.auth.UserContextHolder;
import com.vestaChrono.linkedin.post_service.dto.PostCreateRequestDto;
import com.vestaChrono.linkedin.post_service.dto.PostDto;
import com.vestaChrono.linkedin.post_service.entity.Post;
import com.vestaChrono.linkedin.post_service.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postDto) {
        PostDto createdPost = postService.createPost(postDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        Long userId = UserContextHolder.getCurrentUserid();
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
//        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();  //ternary operation ? :
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId) {
        List<PostDto> posts = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }
}
