package com.five.employnet.controller;

import com.five.employnet.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${wechat.avatar-base-path}")
    private String avatarBasePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {

        String originalFileName = file.getOriginalFilename();
        String suffix = Objects.requireNonNull(originalFileName).substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;

        File dir = new File(avatarBasePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(avatarBasePath + fileName));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return R.success(fileName);
    }

    @GetMapping("/downloadImage")
    public void downloadImage(String name, HttpServletResponse response) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(avatarBasePath + name));

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @GetMapping("/downloadPdf")
    public void downloadPdf(String name, HttpServletResponse response) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(avatarBasePath + name));

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
