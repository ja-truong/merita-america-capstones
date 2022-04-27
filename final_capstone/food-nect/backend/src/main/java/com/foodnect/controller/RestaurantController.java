package com.foodnect.controller;

import com.foodnect.entities.Restaurant;
import com.foodnect.services.RestaurantServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    RestaurantServices restaurantServices;

    @GetMapping("/search/z/{zipCode}")
    public List<Restaurant> findRestaurantsByZipCode(@PathVariable String zipCode) {
        return restaurantServices.findRestaurantsByZipCode(zipCode);
    }

    @GetMapping("/search/c/{city}")
    public List<Restaurant> findRestaurantsByCity(@PathVariable String city) {
        return restaurantServices.findRestaurantsByCity(city);
    }

    @GetMapping("/search/uz/{zipCode}")
    public List<Restaurant> findRestaurantsByZipCodeInAddress(@PathVariable String zipCode) {
        return restaurantServices.findRestaurantsByZipCodeContaining(zipCode);
    }

    @GetMapping("/search/uc/{city}")
    public List<Restaurant> findRestaurantsByCityInAddress(@PathVariable String city) {
        return restaurantServices.findRestaurantsByCityContaining(city);
    }

    @GetMapping("/search")
    public List<Restaurant> findRestaurantsByCompleteAddress(@RequestParam String completeAddress) {
        return restaurantServices.findRestaurantsByCompleteAddressContaining(completeAddress);
    }

    @GetMapping("")
    public List<Restaurant> displayAll() {
        return restaurantServices.displayAll();
    }

    @GetMapping("/{id}")
    public Optional<Restaurant> displayById(@PathVariable int id) {
        return restaurantServices.displayById(id);
    }
}
