package com.birse.kafka.sender;

import com.birse.kafka.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private KafkaTemplate<Long, Project> kafkaTemplate;

    @Value("${app.topic.foo}")
    private String topic;

    public void send(Project project){
        LOG.info("sending message='{}' to topic='{}'", project, topic);
        kafkaTemplate.send(topic, project.getId(),project);
    }
}