package com.repository;

import com.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    List<Restaurant> findByZipCode(String zipCode);

    List<Restaurant> findByCity(String city);
    
    List<Restaurant> findByZipCodeContaining(String zipCode);

    List<Restaurant> findByCityContaining(String city);
}
