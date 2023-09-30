package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Job;

public interface JobService extends IService<Job> {
    void saveJob(Job job);
    void completeJob(Job job);
    Job update(Job newJob);
}
