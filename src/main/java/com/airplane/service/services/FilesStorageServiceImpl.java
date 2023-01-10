package com.airplane.service.services;

import com.cloudinary.Cloudinary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.cloudinary.utils.ObjectUtils.asMap;
import static java.util.Collections.emptyMap;
import static org.springframework.util.ObjectUtils.*;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    private final Path root = Paths.get("uploads");
    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }
    @Override
    public Optional<String> save(MultipartFile file) {
        String fileName = UUID.randomUUID()+file.getOriginalFilename().replaceAll("\\s", "_");
        try {
            Files.copy(file.getInputStream(), this.root.resolve(fileName));
        } catch (Exception e) {
            return Optional.of(null);
        }
        return Optional.of(fileName);
    }

    @Override
    public String uploadFile(MultipartFile file) {
        Map uploadResult = null;
        try {
                Cloudinary cloudinary = new Cloudinary(asMap(
                "cloud_name", "dzavgoc9w",
                "api_key", "842688657531372",
                "api_secret", "-djtDm1NRXVtjZ3L-HGaLfYnNBw",
                "secure", true));


            uploadResult = cloudinary.uploader().upload(file.getBytes(), emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return   uploadResult.get("url").toString();

    }

//    @Override
//    public String uploadFile(MultipartFile file) throws RuntimeException, IOException {
//        Cloudinary cloudinary = new Cloudinary(asMap(
//                "cloud_name", "dzavgoc9w",
//                "api_key", "842688657531372",
//                "api_secret", "-djtDm1NRXVtjZ3L-HGaLfYnNBw",
//                "secure", true));
//        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), emptyMap());
//        return   uploadResult.get("url").toString();
//
//    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}