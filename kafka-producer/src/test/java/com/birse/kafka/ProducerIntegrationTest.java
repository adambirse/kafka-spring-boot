package com.birse.kafka;

import com.birse.kafka.project.Project;
import com.birse.kafka.sender.Sender;
import kafka.Kafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class ProducerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerIntegrationTest.class);

    private static String SENDER_TOPIC = "projects";

    @Autowired
    private Sender sender;

    private KafkaMessageListenerContainer<Long, Project> container;

    private BlockingQueue<ConsumerRecord<Long, Project>> records;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, SENDER_TOPIC);

    @Before
    public void setUp() throws Exception {
        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("sender", "false", embeddedKafka);



        JsonDeserializer deserializer = new JsonDeserializer();
        deserializer.addTrustedPackages("com.birse.kafka.project");
        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<Long, Project> consumerFactory =
                new DefaultKafkaConsumerFactory<Long, Project>(consumerProperties, new LongDeserializer(), deserializer);


        // set the topic that needs to be consumed
        ContainerProperties containerProperties = new ContainerProperties(SENDER_TOPIC);

        // create a Kafka MessageListenerContainer

        container = new KafkaMessageListenerContainer<Long,Project>(consumerFactory, containerProperties);

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

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }

    @After
    public void tearDown() {
        container.stop();
    }

    @Test
    public void testSend() throws InterruptedException {
        // send the message

        Project test = getTestProject();

        sender.send(test);

        // check that the message was received
        ConsumerRecord<Long, Project> received = records.poll(10, TimeUnit.SECONDS);
        // Hamcrest Matchers to check the value
        assertThat(received.value(), samePropertyValuesAs(test));
    }

    private Project getTestProject() {
        return new Project("Test", true, 1L);
    }
}

