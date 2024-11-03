package com.vestaChrono.linkedin.connections_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic sendConnectionTopic() {
        return new NewTopic("send-connection-request-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic acceptConnectionTopic() {
        return new NewTopic("accept-connection-request-topic", 3, (short) 1);
    }

}
