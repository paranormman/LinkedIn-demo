package com.vestaChrono.linkedin.notification_service.consumer;

import com.vestaChrono.linkedin.connections_service.event.AcceptConnectionRequest;
import com.vestaChrono.linkedin.connections_service.event.SendConnectionRequestEvent;
import com.vestaChrono.linkedin.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        log.info("handle connections: handleSendConnectionRequest: {}", sendConnectionRequestEvent);
        String message =
                "You have received a connection request from user with Id: %d "+sendConnectionRequestEvent.getSenderId();
        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequestEvent(AcceptConnectionRequest acceptConnectionRequest) {
        log.info("handle connections: handleAcceptConnectionRequestEvent: {}", acceptConnectionRequest);
        String message =
                "Your connection request has been accepted by the user: %d"+acceptConnectionRequest.getReceiverId();
        sendNotification.send(acceptConnectionRequest.getSenderId(), message);
    }

}
