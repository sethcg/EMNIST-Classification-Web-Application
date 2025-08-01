package emnist.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import emnist.app.service.NetworkService;
import emnist.app.service.notification.NotificationService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class NetworkController {

    @PostMapping("train")
    public void train() {
        NetworkService.train();
    }

    @PostMapping("test")
    public void test() {
        NetworkService.test();
    }

    @PostMapping(value = "predict", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int predict(@RequestBody float[][] image) {
        return NetworkService.predict(image);
    }

    @GetMapping(value = "notification", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter notification() {
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
