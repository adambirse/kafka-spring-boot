package com.birse.kafka.sender;

import com.birse.kafka.project.Project;
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

        sender.send(createProject());
    }

    private Project createProject() {
        Project p = new Project();
        p.setId(RandomIDGenerator.generateLong());
        p.setName("My Project");
        p.setSubmitted(true);
        return p;
    }
}
