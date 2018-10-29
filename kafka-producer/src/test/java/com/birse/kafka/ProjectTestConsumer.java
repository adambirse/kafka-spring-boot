package com.birse.kafka;

import com.birse.kafka.project.Project;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProjectTestConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectTestConsumer.class);

    private KafkaMessageListenerContainer<Long, Project> container;

    private BlockingQueue<ConsumerRecord<Long, Project>> records;

    private KafkaEmbedded embeddedKafka;

    ProjectTestConsumer(KafkaEmbedded embeddedKafka, String topic) {

        this.embeddedKafka = embeddedKafka;


        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false", embeddedKafka);


        JsonDeserializer deserializer = new JsonDeserializer();
        deserializer.addTrustedPackages("com.birse.kafka.project");
        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<Long, Project> consumerFactory =
                new DefaultKafkaConsumerFactory<Long, Project>(consumerProperties, new LongDeserializer(), deserializer);


        // set the topic that needs to be consumed
        ContainerProperties containerProperties = new ContainerProperties(topic);

        // create a Kafka MessageListenerContainer

        container = new KafkaMessageListenerContainer<Long, Project>(consumerFactory, containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container.setupMessageListener(new MessageListener<Long, Project>() {
            @Override
            public void onMessage(ConsumerRecord<Long, Project> record) {
                LOGGER.debug("test-listener received message='{}'", record.toString());
                records.add(record);
            }
        });

    }

    public void start() throws Exception {

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }

    public void stop() {
        container.stop();
    }

    public ConsumerRecord<Long, Project> poll() throws InterruptedException {
        return records.poll(10, TimeUnit.SECONDS);
    }

}
