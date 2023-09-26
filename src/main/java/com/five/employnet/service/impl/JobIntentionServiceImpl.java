package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.JobIntention;
import com.five.employnet.mapper.JobIntentionMapper;
import com.five.employnet.service.JobIntentionService;
import org.springframework.stereotype.Service;

@Service
public class JobIntentionServiceImpl extends ServiceImpl<JobIntentionMapper, JobIntention> implements JobIntentionService {
}
