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

    @Override
    @Transactional
    public void saveJob(Job job, Company company) {
        this.save(job);
    }

    @Override
    @Transactional
    public Job updateJob(Job newJob) {
        this.updateById(newJob);
        return newJob;
    }
}
