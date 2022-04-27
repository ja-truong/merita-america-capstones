package com.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="restaurant_tbl")

public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name="id")
    private int id;
    private String restaurantName;
    private int restaurantType;
    private String completeAddress;
    private String zipCode;
    private String city;
    private String imagePath;
    private String telnum;
    private Boolean isVegan;
    private long openFrom;
    private long openTo;
}