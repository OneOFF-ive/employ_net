package com.five.employnet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

//@ControllerAdvice
//@RestController
//@Slf4j
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleGenericException(Exception ex) {
//        log.warn(Arrays.toString(ex.getStackTrace()));
//        // 处理通用异常
//        return ResponseEntity.status(400).body("错误数据");
//    }

//    @ExceptionHandler(MyCustomException.class)
//    public ResponseEntity<String> handleCustomException(MyCustomException ex) {
//        // 处理自定义异常
//        return ResponseEntity.status(400).body("Custom error: " + ex.getMessage());
//    }
//}
