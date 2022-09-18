package com.airplane.service.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlightsDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightID;
    private Date insetionDate;
    private String flightName;
    @JsonProperty("to")
    private String toLevel;
    private String destination;
    @JsonProperty("time")
    private String flightTime;
    private Double price;
    private String description;
    private String company;
}
