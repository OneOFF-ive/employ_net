package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.JobMessage;
import com.five.employnet.mapper.JobMessageMapper;
import com.five.employnet.service.JobMessageService;
import org.springframework.stereotype.Service;

@Service
public class JobMessageServiceImpl extends ServiceImpl<JobMessageMapper, JobMessage> implements JobMessageService {
}
