package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GetUserTicketsResponseDTO {
    //
    private Long TicketID;
    private Long UserID;
    private Long FlightID;
    private String FlightName;
    private String Company;
    private String To;
    //Destination, FlightDate, Time, PaymentType,CardNo, UPIID, Price, Status, SeatClass
    private String Destination;
    private String FlightDate;
    private String Time;
    private String PaymentType; //Cart,UPI,Cash
    private String CardNo;
    private String UPIID;
    private Double Price;
    private String Status;
    private String SeatClass; //Economy, Business
}
