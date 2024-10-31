package com.vestaChrono.linkedin.post_service.event;

import lombok.*;

@Data
@Builder
public class PostCreatedEvent {

    Long creatorId;
    String content;
    Long postId;

}
