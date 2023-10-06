package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.JobRequest;
import com.five.employnet.mapper.JobRequestMapper;
import com.five.employnet.service.JobRequestService;
import org.springframework.stereotype.Service;

@Service
public class JobRequestServiceImpl extends ServiceImpl<JobRequestMapper, JobRequest> implements JobRequestService {
}
