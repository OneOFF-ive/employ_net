package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.R;
import com.five.employnet.entity.Interview;
import com.five.employnet.service.InterviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/interview")
public class InterviewController {
    final private InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public R<List<Interview>> getInterview() {
        String userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Interview> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Interview::getUser_id, userId);
        List<Interview> interviewList = interviewService.list(queryWrapper);
        return R.success(interviewList);
     }

     @PostMapping
     public R<Interview> saveInterview(@RequestBody Interview interview) {
         String userId = BaseContext.getCurrentId();
         interview.setUser_id(userId);
         interviewService.save(interview);
         return R.success(interview);
     }
}
