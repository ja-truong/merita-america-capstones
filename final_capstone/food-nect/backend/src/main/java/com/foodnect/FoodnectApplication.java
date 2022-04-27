package com.foodnect;

import com.foodnect.entities.Restaurant;
import com.foodnect.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FoodnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodnectApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RestaurantRepository restaurantRepository) {
		return args -> {
			Restaurant resto1 = new Restaurant(
					1, "resto1",
					"Fast Food", "1224 Main St., Va. 24134",
					"24134", "Pearisburg", "https://images.pexels.com/photos/262978/pexels-photo-262978.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500",
					"2172746823", false, "new resto in our block",
					"11:30", "21:00", null
			);
			restaurantRepository.save(resto1);
		};
	}

}
