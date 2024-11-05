package com.vestaChrono.linkedin.notification_service.service;

import com.vestaChrono.linkedin.notification_service.entity.Notification;
import com.vestaChrono.linkedin.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendNotification {

    private final NotificationRepository notificationRepository;

    public void send(Long userId, String message) {
//        creating a notification entity object
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUserId(userId);

//        save the set notification in the repository so that the user can access it later.
        notificationRepository.save(notification);
        log.info("Notification saved for user: {}", userId);
    }

}
