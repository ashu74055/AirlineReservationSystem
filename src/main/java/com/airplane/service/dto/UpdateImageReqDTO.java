package com.airplane.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UpdateImageReqDTO {
    private Long UserID;
    private MultipartFile File;
}
