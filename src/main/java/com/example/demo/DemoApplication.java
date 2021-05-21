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
			devRepo.save(new Device("NodeMCU1", "192.168.100.19"));
			devRepo.save(new Device("NodeMCU2", "192.168.100.22"));
			devRepo.save(new Device("NodeMCU3", "192.168.100.35"));
			devRepo.save(new Device("NodeMCU4", "192.168.100.15"));

			sensorRepo.save(new SensorData(1L, "DHT11", "Temperature", "18"));
			sensorRepo.save(new SensorData(1L, "DHT11", "Temperature", "19"));
			sensorRepo.save(new SensorData(1L, "DHT11", "Temperature", "20"));
			sensorRepo.save(new SensorData(1L, "DHT11", "Temperature", "21"));
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

