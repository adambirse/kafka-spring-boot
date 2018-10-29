package com.birse.kafka;

import com.birse.kafka.project.Project;
import com.birse.kafka.sender.Sender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class ProducerIntegrationTest {


    @Autowired
    private Sender sender;

    private ProjectTestConsumer testConsumer;


    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "projects");

    @Before
    public void setUp() throws Exception {
        testConsumer = new ProjectTestConsumer(embeddedKafka, "projects");
        testConsumer.start();
    }

    @After
    public void tearDown() {
        testConsumer.stop();
    }

    @Test
    public void testSend() throws InterruptedException {

        Project test = getTestProject();
        sender.send(test);

        ConsumerRecord<Long, Project> received = testConsumer.poll();
        assertThat(received.value(), samePropertyValuesAs(test));
    }

    private Project getTestProject() {
        return new Project("Test", true, 1L);
    }
}

