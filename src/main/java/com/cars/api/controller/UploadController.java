package com.cars.api.controller;

import com.cars.api.upload.UploadInput;
import com.cars.api.upload.UploadOutput;
import com.cars.api.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @Secured({"ROLE_ADMIN"})
    @PostMapping
    public ResponseEntity upload(@RequestBody UploadInput uploadInput) throws IOException {

        String url = uploadService.upload(uploadInput);

        return ResponseEntity.ok(new UploadOutput(url));
    }

}
