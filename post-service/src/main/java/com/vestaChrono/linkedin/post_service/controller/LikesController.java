package com.vestaChrono.linkedin.post_service.controller;

import com.vestaChrono.linkedin.post_service.services.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postLikeService.likePost(postId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        postLikeService.unlikePost(postId);
        return ResponseEntity.noContent().build();
    }

}
