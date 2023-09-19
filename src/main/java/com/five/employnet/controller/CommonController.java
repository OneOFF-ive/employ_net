package com.five.employnet.controller;

import com.five.employnet.common.R;
import com.five.employnet.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${wechat.avatar-base-path}")
    private String avatarBasePath;
    @Value("${wechat.annex-base-path}")
    private String annexBasePath;

    final
    CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @PostMapping("/uploadImage")
    public R<String> uploadImage(MultipartFile file) {
        return commonService.upload(file, avatarBasePath);
    }

    @PostMapping("/uploadPdf")
    public R<String> uploadPdf(MultipartFile file) {
        return commonService.upload(file, annexBasePath);
    }


    @GetMapping("/downloadImage")
    public void downloadImage(String name, HttpServletResponse response) {
        commonService.download(name, response, avatarBasePath);
    }

    @GetMapping("/downloadPdf")
    public void downloadPdf(String name, HttpServletResponse response) {
        commonService.download(name, response, annexBasePath);
    }
}
