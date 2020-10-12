package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@SpringBootApplication
public class DemoApplication {
//	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		return args -> {
//
//			System.out.println("Beans provided by Spring Boot:");
//
//			String[] beanNames = ctx.getBeanDefinitionNames();
//			Arrays.sort(beanNames);
//			for (String beanName : beanNames) {
//				System.out.println(beanName);
//			}
//
//		};
//	}

//	@Bean
//	public CommandLineRunner demo(StudentRepo repository) {
//		return (args) -> {
//			// save a few Students
//			repository.save(new Student("Jack"));
//			repository.save(new Student("Chloe"));
//			repository.save(new Student("Kim"));
//			repository.save(new Student("David"));
//			repository.save(new Student("Michelle"));
//
//			// fetch all Students
//			log.info("Students found with findAll():");
//			log.info("-------------------------------");
//			for (Student Student : repository.findAll()) {
//				log.info(Student.toString());
//			}
//			log.info("");
//
//			// fetch an individual Student by ID
//			Student Student = repository.findById(1L);
//			log.info("Student found with findById(1L):");
//			log.info("--------------------------------");
//			log.info(Student.toString());
//			log.info("");
//
//			// fetch Students by last name
////			log.info("Student found with findByLastName('Bauer'):");
////			log.info("--------------------------------------------");
////			repository.findByLastName("Bauer").forEach(bauer -> {
////				log.info(bauer.toString());
////			});
//			// for (Student bauer : repository.findByLastName("Bauer")) {
//			//  log.info(bauer.toString());
//			// }
//			log.info("");
//		};
//	}
}

