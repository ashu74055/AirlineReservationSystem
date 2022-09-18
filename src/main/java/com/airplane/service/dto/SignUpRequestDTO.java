package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;
    private String Role;
    private String MasterPassword;
}
