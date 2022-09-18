package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GetFlightDetailsRequestDTO {
    private int pageNumber;
    private int numberOfRecordPerPage;
    private String type;
}
