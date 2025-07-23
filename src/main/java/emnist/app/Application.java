package emnist.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;

import emnist.app.service.notification.Notification;
import emnist.app.service.notification.NotificationSerializer;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
	public class JacksonCustomConfig {
		@Bean
		SimpleModule notificationModule() {
			SimpleModule module = new SimpleModule();
			module.addSerializer(Notification.class, new NotificationSerializer());
			return module;
		}
	}

}
