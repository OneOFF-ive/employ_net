package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Job;

public interface JobService extends IService<Job> {
    public void saveJob(Job job);
}
