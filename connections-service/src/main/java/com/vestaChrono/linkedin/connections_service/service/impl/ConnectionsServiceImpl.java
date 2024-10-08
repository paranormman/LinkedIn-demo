package com.vestaChrono.linkedin.connections_service.service.impl;

import com.vestaChrono.linkedin.connections_service.entity.Person;
import com.vestaChrono.linkedin.connections_service.repository.PersonRepository;
import com.vestaChrono.linkedin.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsServiceImpl implements ConnectionsService {

    private final PersonRepository personRepository;

    public List<Person> getFirsDegreeConnection(Long userId) {
        log.info("Getting first degree connects for user with id: {}", userId);

        return personRepository.getFirstDegreeConnections(userId);
    }

}
