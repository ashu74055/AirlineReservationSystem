package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserTicketReqDTO {
    private Long userID;
    private int pageNumber;
    private int numberOfRecordPerPage;
}
