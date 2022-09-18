package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GetFeedbackDTO {
    private Long feedbackID;
    private Date insertionDate;
    private String feedback;
    private String userName;
}
