package ua.com.paw.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.com.paw.entity.Image;
import ua.com.paw.exceptions.ExceptionMessages;
import ua.com.paw.repositories.ImageRepository;

import java.io.ByteArrayInputStream;

@RestController
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/image/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id){
        Image image = imageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.IMAGE_WAS_NOT_FOUND));

        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

}
