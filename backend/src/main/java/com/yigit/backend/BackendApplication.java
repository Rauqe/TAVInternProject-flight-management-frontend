package com.yigit.backend;

import com.yigit.backend.dto.RegisterRequest;
import com.yigit.backend.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			UserRepository userRepository
	) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				System.out.println("No admin user found, creating one...");
				var admin = RegisterRequest.builder()
						.username("admin")
						.password("admin")
						.role(Role.ADMIN)
						.build();
				service.register(admin);
				System.out.println("Admin user created.");
			}
		};
	}
}
