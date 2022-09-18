package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class BasicResponseDTO<T> {
    private boolean IsSuccess;
    private String Message;
    private T data;
}
