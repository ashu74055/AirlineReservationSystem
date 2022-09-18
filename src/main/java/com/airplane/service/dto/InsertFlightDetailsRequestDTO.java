package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class InsertFlightDetailsRequestDTO {
    private String flightName;
    private String to;
    private String destination;
    private String time;
    private double price;
    private String company;
    private String description;
}
