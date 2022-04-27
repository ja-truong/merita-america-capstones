package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.web.client.RestTemplate;

public class UserService {

    private RestTemplate restTemplate = new RestTemplate();
    private String userPath = "users/";

    public User[] getUserName(String url) {

        return restTemplate.getForObject(url + userPath, User[].class);
    }
}
