package com.vestaChrono.linkedin.post_service.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class PostDto {

    private Long id;

    private String content;

    private Long userId;

    private LocalDateTime createdAt;
}
