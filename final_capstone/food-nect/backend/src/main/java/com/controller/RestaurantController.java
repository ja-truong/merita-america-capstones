package com.controller;

import com.entities.Restaurant;
import com.repository.RestaurantRepository;
import com.services.RestaurantServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodnect/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantServices restaurantServices;

    @GetMapping("/search/{zipCode}")
    public List<Restaurant> findRestaurantsByZipCode(@PathVariable String zipCode) {
        return restaurantServices.findRestaurantsByZipCode(zipCode);
    }

    @GetMapping("/search/{city}")
    public List<Restaurant> findRestaurantsByCity(@PathVariable String city) {
        return restaurantServices.findRestaurantsByCity(city);
    }

    @GetMapping("/search/u/{zipCode}")
    public List<Restaurant> findRestaurantsByZipCodeInAddress(@PathVariable String zipCode) {
        return restaurantServices.findRestaurantsByZipCodeContaining(zipCode);
    }

    @GetMapping("/search/u/{city}")
    public List<Restaurant> findRestaurantsByCityInAddress(@PathVariable String city) {
        return restaurantServices.findRestaurantsByCityContaining(city);
    }
}
