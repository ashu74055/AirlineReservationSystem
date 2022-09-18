package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PaginationResponseDTO <T> {
    private Boolean isSuccess;
    private String message;
    private int currentPage;
    private double totalRecords;
    private int totalPage;
    private T data;
}
