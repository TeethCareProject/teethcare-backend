package com.teethcare.controller;

import com.teethcare.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/api/files")
    public ResponseEntity<String> create(@RequestBody MultipartFile[] files) {

        for (MultipartFile file : files) {

            try {

                String fileName = fileService.save(file);

                String imageUrl = fileService.getImageUrl(fileName);
                return new ResponseEntity<>(imageUrl, HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        return ResponseEntity.ok().build();
    }
}
