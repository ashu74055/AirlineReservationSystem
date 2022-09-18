package com.airplane.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPersonalDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalDetailID;
    private Long userID;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String homePhone;
    private String personalNumber;
    private String email;
    private String gender;
    private String dob;
    private String occupation;
    private String imageUrl;
    private String publicID;
}