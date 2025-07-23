package emnist.app.service.notification;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class NotificationService {

    private static final Collection<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    
    public static void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public static void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    public static void sendNotification(String eventName, Notification notification) {
        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();
        emitters.forEach(emitter -> {
            try {
                String jsonString = NotificationSerializer.getJsonString(notification);
                emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(jsonString, MediaType.TEXT_EVENT_STREAM));
            } catch (IOException exception) {
                // HANDLE DISCONNECT
                emitter.completeWithError(exception);
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }

}
