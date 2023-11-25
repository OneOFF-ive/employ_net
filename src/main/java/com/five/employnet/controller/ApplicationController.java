package com.five.employnet.controller;

import com.five.employnet.config.CorsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("application")
//@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class ApplicationController {
}
