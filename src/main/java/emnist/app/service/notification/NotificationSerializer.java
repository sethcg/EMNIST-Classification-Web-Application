package emnist.app.service.notification;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NotificationSerializer extends JsonSerializer<Notification> {

    public static String getJsonString(Notification notification) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(notification);
    }

    @Override
    public void serialize(Notification notification, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("epochNum", Integer.toString(notification.epochNum));
        jsonGenerator.writeStringField("batchNum", Integer.toString(notification.batchNum));
        jsonGenerator.writeStringField("steps", Integer.toString(notification.steps));
        jsonGenerator.writeStringField("loss", notification.loss);
        jsonGenerator.writeStringField("accuracy", notification.accuracy);
        jsonGenerator.writeEndObject();
    }
}
