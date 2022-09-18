package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PaymentGatwayReqDTO {
    private Long userID;
    private Long flightID;
    private String flightDate;
    private String seatClass;
    private String paymentType;
    private String cartNo;
    private String upiid;
    private String status;
    private double price;
}
