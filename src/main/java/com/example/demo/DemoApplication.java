package com.example.demo;

import com.example.demo.Model.Device;
import com.example.demo.Repository.DeviceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class DemoApplication {
//	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(DeviceRepository devRepo) {
		return (args) -> {
			Device device = new Device("NodeMCU1", "192.168.100.19");
			devRepo.save(device);
		};
	}
}

