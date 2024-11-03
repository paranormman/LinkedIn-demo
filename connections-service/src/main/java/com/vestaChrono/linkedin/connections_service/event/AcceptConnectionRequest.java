package com.vestaChrono.linkedin.connections_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcceptConnectionRequest {

    private Long senderId;
    private Long receiverId;

}
