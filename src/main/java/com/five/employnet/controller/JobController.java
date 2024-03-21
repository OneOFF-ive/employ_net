package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.entity.Company;
import com.five.employnet.entity.Job;
import com.five.employnet.service.CompanyService;
import com.five.employnet.service.JobService;
import com.five.employnet.service.JobViewService;
import com.five.employnet.view.JobView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/job")
//@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class JobController {
    private final JobService jobService;
    private final JobViewService jobViewService;
    private final CompanyService companyService;

    public JobController(JobService jobService, JobViewService jobViewService, CompanyService companyService) {
        this.jobService = jobService;
        this.jobViewService = jobViewService;
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
        } else return R.error("公司不存在");
    }

    @GetMapping("/page")
    public R<Page<JobView>> getPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, String prompt) {
        Page<JobView> pageInfo = new Page<>(page, pageSize);
        QueryWrapper<JobView> jobLambdaQueryWrapper = new QueryWrapper<>();
        jobLambdaQueryWrapper
                .like(prompt != null, "job_lab", prompt)
                .or()
                .like(prompt != null, "company_class", prompt)
                .or()
                .like(prompt != null, "company", prompt)
                .or()
                .like(prompt != null, "business", prompt)
                .or()
                .like(prompt != null, "address", prompt)
                .or()
                .like(prompt != null, "title", prompt)
                .orderBy(true, false, "RAND()");
        jobViewService.page(pageInfo, jobLambdaQueryWrapper);

        return R.success(pageInfo);
    }

    @GetMapping("/latest")
    public R<Page<JobView>> getLatest(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        Page<JobView> pageInfo = new Page<>(page, pageSize);
        QueryWrapper<JobView> jobLambdaQueryWrapper = new QueryWrapper<>();
        jobLambdaQueryWrapper
                .orderBy(true, false, "update_time");
        jobViewService.page(pageInfo, jobLambdaQueryWrapper);

        return R.success(pageInfo);
    }

    @GetMapping("/near")
    public R<Page<JobView>> getNear(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("city") String city) {
        Page<JobView> pageInfo = new Page<>(page, pageSize);
        QueryWrapper<JobView> jobLambdaQueryWrapper = new QueryWrapper<>();
        jobLambdaQueryWrapper
                .like("city", city)
                .orderBy(true, false, "RAND()");
        jobViewService.page(pageInfo, jobLambdaQueryWrapper);

        return R.success(pageInfo);
    }

    @GetMapping("/recommend")
    public R<Page<JobView>> getRecommend(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("keys") List<String> keys) {
        Page<JobView> pageInfo = new Page<>(page, pageSize);
        QueryWrapper<JobView> jobLambdaQueryWrapper = new QueryWrapper<>();
        jobLambdaQueryWrapper
                .like("job_lab", keys.get(0))
                .or()
                .like("job_lab", keys.get(1))
                .or()
                .like("job_lab", keys.get(2));
        jobViewService.page(pageInfo, jobLambdaQueryWrapper);

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
            String companyId = company.getCompany_id();
            LambdaQueryWrapper<Job> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Job::getCompany_id, companyId);
            List<Job> jobList = jobService.list(queryWrapper);
            return R.success(jobList);
        }
        return R.success(null);
    }

    @PostMapping("/save")
    public R<Job> saveOne(@RequestBody Job job) {
        jobService.save(job);
        return R.success(job);
    }

    @DeleteMapping("/delete")
    public R<String> deleteByIds(@RequestParam List<String> ids) {
        jobService.removeByIds(ids);
        return R.success("删除成功");
    }

    @GetMapping("/count")
    public R<Integer> count() {
        return R.success(jobService.count());
    }

    @GetMapping("/get/job")
    public R<Job> getJob(@RequestParam("job_id") String id) {
        LambdaQueryWrapper<JobView> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, JobView::getJob_id, id);
        Job job = jobViewService.getOne(queryWrapper);
        return R.success(job);
    }

    @GetMapping("/from/company")
    public R<List<JobView>> getCompanyJobList(@RequestParam("company_id") String company_id) {
        QueryWrapper<JobView> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("company_id", company_id)
                .orderBy(true, false, "update_time");
        List<JobView> jobList = jobViewService.list(queryWrapper);
        return R.success(jobList);
    }


}
