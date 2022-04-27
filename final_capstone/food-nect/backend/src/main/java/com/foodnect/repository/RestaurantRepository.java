package com.foodnect.repository;

import com.foodnect.entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    List<Restaurant> findByZipCode(String zipCode);

    List<Restaurant> findByCity(String city);
    
    List<Restaurant> findByZipCodeContaining(String zipCode);

    List<Restaurant> findByCityContaining(String city);

    List<Restaurant> findByCompleteAddressContaining(String completeAddress);
}
