package com.five.employnet.service;

import com.five.employnet.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class CommonService {
    public R<String> upload(MultipartFile file, String basePath) {
        String originalFileName = file.getOriginalFilename();
        String suffix = Objects.requireNonNull(originalFileName).substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;

        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            log.error(e.getMessage());
            return R.error("保存失败");
        }
        return R.success(fileName);
    }

    public void download(String name, HttpServletResponse response, String basePath) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(basePath + name));

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
}
