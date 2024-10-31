package com.vestaChrono.linkedin.notification_service.consumer;

import com.vestaChrono.linkedin.notification_service.NotificationServiceApplication;
import com.vestaChrono.linkedin.notification_service.clients.ConnectionClient;
import com.vestaChrono.linkedin.notification_service.dto.PersonDto;
import com.vestaChrono.linkedin.notification_service.entity.Notification;
import com.vestaChrono.linkedin.notification_service.repository.NotificationRepository;
import com.vestaChrono.linkedin.post_service.event.PostCreatedEvent;
import com.vestaChrono.linkedin.post_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.internals.Topic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

    private final ConnectionClient connectionClient;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Sending Notification: handlePostCreated: {}", postCreatedEvent);
//        get the first-degree connections of the user using Feign client
        List<PersonDto> connections = connectionClient.getFirstConnections(postCreatedEvent.getCreatorId());

//        Send Notification to all the first-degree connections of the creator.
        for (PersonDto connection: connections) {
            sendNotification(connection.getUserId(), "Your connection "+ postCreatedEvent.getCreatorId()+ " has created a post," +
                    " check it out");
        }

    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Sending Notification: handlePostLiked: {}", postLikedEvent);

//        send notification to the post owner when someone likes their post
        String message = String.format("Your post, %d has been liked by %d", postLikedEvent.getPostId(),
                postLikedEvent.getLikedByUserId());

        sendNotification(postLikedEvent.getCreatorId(), message);

    }

    public void sendNotification(Long userId, String message) {
//        creating a notification entity object
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUserId(userId);

//        save the set notification in the repository so that the user can access it later.
        notificationRepository.save(notification);
    }

}
