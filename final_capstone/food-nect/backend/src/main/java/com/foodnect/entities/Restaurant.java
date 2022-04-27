package com.foodnect.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="restaurant_tbl")
@Data
@NoArgsConstructor

public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name="id")
    private int id;
    private String restaurantName;
    private String restaurantType;
    private String completeAddress;
    private String zipCode;
    private String city;
    private String imagePath;
    private String telnum;
    private Boolean isVegan;
    private String description;
    private String openFrom;
    private String openTo;

    @OneToMany
    @JoinColumn(name = "id")
    private Set<RestoFromLink> restoFromLinkSet;

    public Restaurant(int id, String restaurantName, String restaurantType, String completeAddress, String zipCode, String city, String imagePath, String telnum, Boolean isVegan, String description, String openFrom, String openTo, Set<RestoFromLink> restoFromLinkSet) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.restaurantType = restaurantType;
        this.completeAddress = completeAddress;
        this.zipCode = zipCode;
        this.city = city;
        this.imagePath = imagePath;
        this.telnum = telnum;
        this.isVegan = isVegan;
        this.description = description;
        this.openFrom = openFrom;
        this.openTo = openTo;
        this.restoFromLinkSet = restoFromLinkSet;
    }
}