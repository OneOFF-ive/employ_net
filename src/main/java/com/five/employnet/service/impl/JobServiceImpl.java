package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Company;
import com.five.employnet.entity.Job;
import com.five.employnet.entity.JobMessage;
import com.five.employnet.mapper.JobMapper;
import com.five.employnet.service.CompanyService;
import com.five.employnet.service.JobMessageService;
import com.five.employnet.service.JobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    final private JobMessageService jobMessageService;
    final private CompanyService companyService;

    public JobServiceImpl(JobMessageService jobMessageService, CompanyService companyService, CompanyService companyService1) {
        this.jobMessageService = jobMessageService;
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
    }

    @Override
    public void completeJob(Job job) {
        String jobId = job.getJob_id();
        LambdaQueryWrapper<JobMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobMessage::getJob_id, jobId);
        JobMessage jobMessage = jobMessageService.getOne(queryWrapper);
        job.setMessage_detail(jobMessage);
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
        LambdaQueryWrapper<JobMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobMessage::getJob_id, jobId);
        JobMessage oldJobMessage = jobMessageService.getOne(queryWrapper);
        if (oldJobMessage != null) {
            String jobMessageId = oldJobMessage.getId();
            newJobMessage.setId(jobMessageId);
            jobMessageService.updateById(newJobMessage);
        } else {
            jobMessageService.save(newJobMessage);
        }
        return newJob;
    }
}
