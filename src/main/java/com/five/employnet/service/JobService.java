package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Company;
import com.five.employnet.entity.Job;

public interface JobService extends IService<Job> {
    void saveJob(Job job, Company company);
    Job updateJob(Job newJob);
}
