package com.vestaChrono.linkedin.connections_service.service.impl;

import com.vestaChrono.linkedin.connections_service.auth.UserContextHolder;
import com.vestaChrono.linkedin.connections_service.entity.Person;
import com.vestaChrono.linkedin.connections_service.event.AcceptConnectionRequest;
import com.vestaChrono.linkedin.connections_service.event.SendConnectionRequestEvent;
import com.vestaChrono.linkedin.connections_service.repository.PersonRepository;
import com.vestaChrono.linkedin.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequest> acceptRequestKafkaTemplate;

    public List<Person> getFirsDegreeConnection() {

        Long userId = UserContextHolder.getCurrentUserid();
        log.info("Getting first degree connects for user with id: {}", userId);

        return personRepository.getFirstDegreeConnections(userId);
    }

    @Override
    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserid();
        log.info("Trying to send connection request, sender: {}, receiver: {}", senderId, receiverId);
//        check if the senderId and receiverId are the same
        if (senderId.equals(receiverId)){
            throw new RuntimeException("Both sender and receiver are the same");
        }
//        check if the request is already sent
        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (alreadySentRequest) {
            throw new RuntimeException("Connection request already exists, cannot send again");
        }
//        check if the user is already connected
        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new RuntimeException("Already connected users, cannot add connection request");
        }
//        sending the connection request
        log.info("Successfully sent the connection request");
        personRepository.addConnectionRequest(senderId, receiverId);

//        send notification to the user to send the connection request.
        log.info("successfully accepted the connection request, sender: {}, receiver: {}", senderId, receiverId );
        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        sendRequestKafkaTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);

        return true;

    }

    @Override
    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserid();

//        check if there is a connection request first
        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId, receiverId);
        if (!connectionRequestExists) {
            throw new RuntimeException("No Connection request exists to accept");
        }

        personRepository.acceptConnectionRequest(senderId, receiverId);

//        send notification to the users using the topic created
        log.info("Successfully sent connection request to the user sender:{}, receiver: {}", senderId, receiverId);
        AcceptConnectionRequest acceptConnectionRequest = AcceptConnectionRequest.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        acceptRequestKafkaTemplate.send("accept-connection-request-topic", acceptConnectionRequest);

        return true;

    }

    @Override
    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserid();

        //        check if there is a connection request first
        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId, receiverId);
        if (!connectionRequestExists) {
            throw new RuntimeException("No Connection request exists, cannot delete");
        }

        personRepository.rejectConnectionRequest(senderId, receiverId);
        return true;
    }

}
