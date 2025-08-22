package emnist.app;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import emnist.app.service.NetworkService;
import emnist.app.service.helper.FileManagement;
import emnist.app.service.notification.NotificationService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class NetworkController {

    @Autowired
    private Environment environment;

    @Autowired
    private NetworkService networkService;

    @PostMapping(value = "ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public String Ping() {
        StringBuilder string = new StringBuilder();
        string.append("[Spring Boot Application info]");
        string.append("\nName: " + environment.getProperty("application.name"));
        string.append("\nVersion: " + environment.getProperty("application.version"));
        return string.toString();
    }

    @PostMapping(value = "hasNetwork")
    public boolean HasNetwork() {
        return FileManagement.hasNetwork();
    }

    @PostMapping("train")
    public void Train() {
        networkService.train();
    }

    @PostMapping("test")
    public void Test() {
        networkService.test();
    }

    @PostMapping(value = "predict", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int Predict(@RequestBody float[][] image) {
        return networkService.predict(image);
    }

    @PostMapping(value = "trainingStats", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> TrainingStats() {
        return networkService.getTrainingStatistics();
    }

    @PostMapping(value = "testingStats", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> TestingStats() {
        return networkService.getTestingStatistics();
    }

    @GetMapping(value = "notification", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter Notification() {
        SseEmitter emitter = new SseEmitter(0L); // NO TIMEOUT

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                NotificationService.addEmitter(emitter);
            } catch (Exception exception) {
                emitter.completeWithError(exception);
            } finally {
                executor.shutdown();
            }
        });

        emitter.onCompletion(() -> NotificationService.removeEmitter(emitter));
        emitter.onTimeout(() -> {
            NotificationService.removeEmitter(emitter);
            emitter.complete();
        });
        emitter.onError((exception) -> NotificationService.removeEmitter(emitter));

        return emitter;
    }

}
