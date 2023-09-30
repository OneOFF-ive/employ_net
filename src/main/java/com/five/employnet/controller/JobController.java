package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.entity.Job;
import com.five.employnet.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public R<Job> save(HttpServletRequest request, @RequestBody Job job) {
        String companyId = BaseContext.getCurrentId();

        job.setCompany_id(companyId);
        jobService.saveJob(job);
        return R.success(job);
    }

    @GetMapping("/page")
    public R<Page<Job>> getPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, String prompt) {
        Page<Job> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Job> jobLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jobLambdaQueryWrapper
                .like(prompt != null, Job::getJob_lab, prompt)
                .or()
                .like(prompt != null, Job::getTitle, prompt);
        jobService.page(pageInfo, jobLambdaQueryWrapper);

        List<Job> jobList = pageInfo.getRecords();
        for (Job job : jobList) {
            jobService.completeJob(job);
        }
        return R.success(pageInfo);
    }

    @PutMapping
    public R<Job> update(@RequestBody Job newJob) {
        return R.success(jobService.update(newJob));
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("job_id") String job_id) {
        jobService.removeById(job_id);
        return R.success("删除成功");
    }
}
