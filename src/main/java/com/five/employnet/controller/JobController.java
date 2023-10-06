package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.R;
import com.five.employnet.entity.Company;
import com.five.employnet.entity.Job;
import com.five.employnet.service.CompanyService;
import com.five.employnet.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {
    private final JobService jobService;
    private final CompanyService companyService;

    public JobController(JobService jobService, CompanyService companyService) {
        this.jobService = jobService;
        this.companyService = companyService;
    }

    @PostMapping
    public R<Job> save(@RequestBody Job job) {
        String userId = BaseContext.getCurrentId();
        Company company = companyService.getCompanyByUserId(userId);
        if (company != null) {
            String companyId = company.getCompany_id();
            job.setCompany_id(companyId);
            jobService.saveJob(job, company);
            return R.success(job);
        }
        else return R.error("公司不存在");
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
        return R.success(jobService.updateJob(newJob));
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("job_id") String job_id) {
        jobService.removeById(job_id);
        return R.success("删除成功");
    }

    @GetMapping
    public R<List<Job>> myJob() {
        String userId = BaseContext.getCurrentId();
        Company company = companyService.getCompanyByUserId(userId);
        if (company != null) {
            String companyId = company.getCompany_id();;
            LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Job::getCompany_id, companyId);
            List<Job> jobList = jobService.list(queryWrapper);
            return R.success(jobList);
        }
        return R.success(null);
    }
}
