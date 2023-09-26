package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.entity.Job;
import com.five.employnet.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;
    final private JwtUtil jwtUtil;

    public JobController(JobService jobService, JwtUtil jwtUtil) {
        this.jobService = jobService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public R<Job> save(HttpServletRequest request, @RequestBody Job job) {
        String authorizationHeader = request.getHeader("Authorization");
        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
        String companyId = jwtUtil.extractUsername(authToken);
        job.setCompany_id(companyId);
        jobService.saveJob(job);
        return R.success(job);
    }

    @GetMapping("/page")
    public R<Page<Job>> getPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, String prompt) {
        Page<Job> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Job> jobLambdaQueryWrapper =new LambdaQueryWrapper<>();
        jobLambdaQueryWrapper.eq(prompt != null, Job::getJob_lab, prompt);
        jobService.page(pageInfo, jobLambdaQueryWrapper);

        List<Job> jobList = pageInfo.getRecords();
        for (Job job: jobList) {
            jobService.completeJob(job);
        }
        return R.success(pageInfo);
    }

}
