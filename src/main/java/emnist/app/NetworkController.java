package emnist.app;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import emnist.app.service.Service;

@RestController
@RequestMapping("/api/network")
@CrossOrigin
public class NetworkController {

    @GetMapping("")
    public String home() {
        Service.TrainNetwork();
        return "DONE TRAINING";
    }

    @GetMapping("test")
    public String runTests() {
        Service.TestNetwork();
        return "DONE TESTING";
    }

}
