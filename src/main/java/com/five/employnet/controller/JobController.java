package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.R;
import com.five.employnet.entity.Job;
import com.five.employnet.entity.JobMessage;
import com.five.employnet.service.JobMessageService;
import com.five.employnet.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;
    private final JobMessageService jobMessageService;

    public JobController(JobService jobService, JobMessageService jobMessageService) {
        this.jobService = jobService;
        this.jobMessageService = jobMessageService;
    }

    @PostMapping
    public R<Job> save(@RequestBody Job job) {
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
            String jobId = job.getJob_id();
            LambdaQueryWrapper<JobMessage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(JobMessage::getJob_id, jobId);
            JobMessage jobMessage = jobMessageService.getOne(queryWrapper);
            job.setMessage_detail(jobMessage);
        }
        return R.success(pageInfo);
    }

}
