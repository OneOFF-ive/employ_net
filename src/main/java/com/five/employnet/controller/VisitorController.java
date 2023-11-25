package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.entity.Visitor;
import com.five.employnet.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/visitor")
@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class VisitorController {
    final private VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/view/total")
    R<Integer> total() {
        int total = visitorService.count();
        return R.success(total);
    }

    @GetMapping("/view/{date}")
    R<Integer> visitsAt(@PathVariable String date) {
        QueryWrapper<Visitor> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("count(*) as count")
                .groupBy("date").having("date = {0}", date);
        Map<String, Object> res = visitorService.getMap(queryWrapper);
        if (res != null) {
            int count = (int) res.get("count");
            return R.success(count);
        }
        return R.success(0);
    }

}
