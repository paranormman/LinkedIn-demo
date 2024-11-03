package com.vestaChrono.linkedin.notification_service.consumer;

import com.vestaChrono.linkedin.connections_service.event.SendConnectionRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConnectionsServiceConsumer {

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {

    }

}
