package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Company;
import com.five.employnet.entity.Job;
import com.five.employnet.entity.JobRequest;
import com.five.employnet.mapper.JobMapper;
import com.five.employnet.service.CompanyService;
import com.five.employnet.service.JobRequestService;
import com.five.employnet.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {
    final private JobRequestService jobRequestService;
    final private CompanyService companyService;

    public JobServiceImpl(JobRequestService jobRequestService, CompanyService companyService) {
        this.jobRequestService = jobRequestService;
        this.companyService = companyService;
    }

    @Override
    @Transactional
    public void saveJob(Job job, Company company) {
        job.setCompany(company.getName());
        job.setBusiness(company.getBusiness());
        job.setAvatar_url(company.getAvatar_url());
        job.setLab(company.getCompany_class());
        this.save(job);
    }

    @Override
    public void completeJob(Job job) {
        String jobId = job.getJob_id();
    }

    @Override
    @Transactional
    public Job updateJob(Job newJob) {
        String jobId = newJob.getJob_id();
        Company company = companyService.getById(newJob.getCompany_id());
        newJob.setCompany(company.getName());
        newJob.setBusiness(company.getBusiness());
        newJob.setAvatar_url(company.getAvatar_url());
        newJob.setLab(company.getCompany_class());
        this.updateById(newJob);
        return newJob;
    }
}
