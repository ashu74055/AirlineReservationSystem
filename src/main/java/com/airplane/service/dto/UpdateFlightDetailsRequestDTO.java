package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFlightDetailsRequestDTO {
    private Long flightID;
    private String flightName;
    private String to;
    private String destination;
    private String time;
    private Double price;
    private String company;
    private String description;
}
