package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
