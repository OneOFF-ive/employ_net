package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Company;
import com.five.employnet.entity.Job;
import com.five.employnet.entity.JobMessage;
import com.five.employnet.entity.JobRequest;
import com.five.employnet.mapper.JobMapper;
import com.five.employnet.service.CompanyService;
import com.five.employnet.service.JobMessageService;
import com.five.employnet.service.JobRequestService;
import com.five.employnet.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    final private JobMessageService jobMessageService;
    final private JobRequestService jobRequestService;
    final private CompanyService companyService;

    public JobServiceImpl(JobMessageService jobMessageService, CompanyService companyService, JobRequestService jobRequestService, CompanyService companyService1) {
        this.jobMessageService = jobMessageService;
        this.jobRequestService = jobRequestService;
        this.companyService = companyService1;
    }

    @Override
    @Transactional
    public void saveJob(Job job, Company company) {
        job.setCompany(company.getName());
        job.setBusiness(company.getBusiness());
        job.setAvatar_url(company.getAvatar_url());
        job.setLab(company.getCompany_class());
        this.save(job);
        JobMessage jobMessage = job.getMessage_detail();
        if (jobMessage != null) {
            jobMessage.setJob_id(job.getJob_id());
            jobMessageService.save(jobMessage);
        }
        JobRequest jobRequest = job.getRequest();
        if (jobRequest != null) {
            jobRequest.setJob_id(job.getJob_id());
            jobRequestService.save(jobRequest);
        }
    }

    @Override
    public void completeJob(Job job) {
        String jobId = job.getJob_id();
        LambdaQueryWrapper<JobMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobMessage::getJob_id, jobId);
        JobMessage jobMessage = jobMessageService.getOne(queryWrapper);
        job.setMessage_detail(jobMessage);

        LambdaQueryWrapper<JobRequest> jobRequestLambdaQueryWrapper = new LambdaQueryWrapper<>();
        jobRequestLambdaQueryWrapper.eq(JobRequest::getJob_id, jobId);
        JobRequest jobRequest = jobRequestService.getOne(jobRequestLambdaQueryWrapper);
        job.setRequest(jobRequest);
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

        JobMessage newJobMessage = newJob.getMessage_detail();
        if (newJobMessage != null) {
            LambdaQueryWrapper<JobMessage> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(JobMessage::getJob_id, jobId);
            JobMessage oldJobMessage = jobMessageService.getOne(queryWrapper);
            if (oldJobMessage != null) {
                String jobMessageId = oldJobMessage.getId();
                newJobMessage.setId(jobMessageId);
                jobMessageService.updateById(newJobMessage);
            } else {
                newJobMessage.setJob_id(jobId);
                jobMessageService.save(newJobMessage);
            }
        }

        JobRequest newJobRequest = newJob.getRequest();
        if (newJobRequest != null) {
            LambdaQueryWrapper<JobRequest> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(JobRequest::getJob_id, jobId);
            JobRequest oldJobRequest = jobRequestService.getOne(queryWrapper);
            if (oldJobRequest != null) {
                String id = oldJobRequest.getId();
                newJobRequest.setId(id);
                jobRequestService.updateById(newJobRequest);
            } else {
                newJobRequest.setJob_id(jobId);
                jobRequestService.save(newJobRequest);
            }
        }
        return newJob;
    }
}
