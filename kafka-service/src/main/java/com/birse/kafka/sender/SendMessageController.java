package com.birse.kafka.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
public class SendMessageController {

    @Autowired
    private Sender sender;

    @GetMapping
    public void send() {
        sender.send("Spring Kafka and Spring Boot Configuration Example");
    }
}
