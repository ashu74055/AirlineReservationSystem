package com.airplane.service.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class FileUploadStorage {

    public static String uploadFile(MultipartFile file) throws RuntimeException, IOException {
        Map uploadResult = null;
        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dzavgoc9w",
                    "api_key", "842688657531372",
                    "api_secret", "-djtDm1NRXVtjZ3L-HGaLfYnNBw",
                    "secure", true));
             uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        }catch (Exception ex){
            return null;
        }
        return   uploadResult.get("url").toString();

    }

}
