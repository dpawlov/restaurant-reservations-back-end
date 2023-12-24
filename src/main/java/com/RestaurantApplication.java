package com;

import com.domain.Role;
import com.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner demo(RoleRepository roleRepo) {
//		return (args) -> {
//			Role role=new Role();
//			role.setName("ROLE_ADMIN");
//			roleRepo.save(role);
//		};
//	}
}
