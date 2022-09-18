package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserManipulateRequestDTO {
    private Long userID;
    private Boolean block;
    private Boolean unblock;
}
