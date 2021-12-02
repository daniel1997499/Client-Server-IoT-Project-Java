package com.example.demo;

import com.example.demo.Model.Device;
import com.example.demo.Model.SensorData;
import com.example.demo.Repository.DeviceRepository;
import com.example.demo.Repository.SensorDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);
	public static void main(String[] args) {
//		log.info("this is a info message");
//		log.warn("this is a warn message");
//		log.error("this is a error message");
		SpringApplication.run(DemoApplication.class, args);
	}

	@Lazy
	@Bean
	public CommandLineRunner demo(DeviceRepository devRepo, SensorDataRepository sensorRepo) {
		return (args) -> {
			//Creating some placeholders
			for (int i = 0; i<10; i++) {
				String name = "NodeMCU";
				name = name + (i+1);
				String address = "192.168.100.";
				address = address + new Random().nextInt(100);
				Device savedDevice = devRepo.save(new Device(name, address));
				log.info("Registered new device: " + savedDevice.getName() + " - " + savedDevice.getAddress());
			}
			for (int j = 0; j<10; j++) {
				String data = "";
				data = String.valueOf(new Random().nextInt(30));
				SensorData savedData = sensorRepo.save(new SensorData(1L, "DHT11", "Temperature", data));
				log.info("Registered new sensor data: " + " - " + savedData.getDeviceId() + " - " + savedData.getSensor() + " - " + " - " + savedData.getDataType() + " - " + savedData.getData());
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

