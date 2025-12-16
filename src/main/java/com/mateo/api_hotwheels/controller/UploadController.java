package com.mateo.api_hotwheels.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;

@RestController
public class UploadController {

    @Value("${upload.dir:/home/ec2-user/api_tienda_spring/uploads/}")
    private String uploadDir;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {

        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        File destino = new File(uploadDir + fileName);
        file.transferTo(destino);

        return "http://100.25.32.238:8080/uploads/" + fileName;
    }
}
