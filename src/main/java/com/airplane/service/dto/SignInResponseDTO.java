package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponseDTO {
    private Boolean IsSuccess;
    private String Message;
    private SignInDTO data;
    private String Token;
}
