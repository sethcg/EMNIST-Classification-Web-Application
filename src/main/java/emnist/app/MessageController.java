package emnist.app;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import emnist.app.service.Service;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin
public class MessageController {

    @GetMapping("")
    public String home() {
        Service.TestNetworkTraining();

        return "Hello, Spring & Vue 👋🏻";
    }
}
