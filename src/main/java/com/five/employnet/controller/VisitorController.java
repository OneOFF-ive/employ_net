package com.five.employnet.controller;

import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/notice")
@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class VisitorController {
    final private VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/total")
    R<Integer> total() {
        int total = visitorService.count();
        return R.success(total);
    }

//    @GetMapping("/{date}")
//    R<Integer> visitsAt(@PathVariable String date) {
//
//    }

}
