package com.vestaChrono.linkedin.connections_service.service;

import com.vestaChrono.linkedin.connections_service.entity.Person;
import java.util.List;

public interface ConnectionsService {

    List<Person> getFirsDegreeConnection();
}
