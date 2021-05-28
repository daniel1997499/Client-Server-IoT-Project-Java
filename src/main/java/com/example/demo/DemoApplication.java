package com.example.demo;

import com.example.demo.Model.Device;
import com.example.demo.Model.SensorData;
import com.example.demo.Repository.DeviceRepository;
import com.example.demo.Repository.SensorDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Random;


@SpringBootApplication
public class DemoApplication {
//	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Lazy
	@Bean
	public CommandLineRunner demo(DeviceRepository devRepo, SensorDataRepository sensorRepo) {
		return (args) -> {
			for (int i = 0; i<5; i++) {
				String name = "NodeMCU";
				name = name + (i+1);
				String address = "192.168.100.";
				address = address + (i+1);
				devRepo.save(new Device(name, address));
			}
			for (int j = 0; j<48; j++) { //every 30 minutes fetch
				String data = "";
				data = String.valueOf(new Random().nextInt(30));
				sensorRepo.save(new SensorData(1L, "DHT11", "Temperature", data));
			}
		};
	}

	@Bean
	@Description("Spring Message Resolver")
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		return messageSource;
	}
}

