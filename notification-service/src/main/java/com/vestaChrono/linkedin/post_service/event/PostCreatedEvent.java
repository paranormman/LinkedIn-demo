package com.vestaChrono.linkedin.post_service.event;

import lombok.*;

@Data
public class PostCreatedEvent {

    Long creatorId;
    String content;
    Long postId;

}
