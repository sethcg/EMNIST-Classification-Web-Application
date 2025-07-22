package emnist.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import emnist.app.service.NotificationService;
import emnist.app.service.NetworkService;

@RestController
@RequestMapping("/api/network")
@CrossOrigin
public class NetworkController {

    @GetMapping("train")
    public void train() {
        NetworkService.train();
    }

    @GetMapping("test")
    public void test() {
        NetworkService.test();
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
