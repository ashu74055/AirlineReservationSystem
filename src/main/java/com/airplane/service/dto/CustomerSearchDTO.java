package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @Data @NoArgsConstructor
public class CustomerSearchDTO {
    private int pageNumber;
    private int numberOfRecordPerPage;
    private boolean isSearch;
    private String to;
    private String destination;
    private boolean isFilter;
    private BaseFilterDTO filterOn;
    private boolean isAscending;
}
