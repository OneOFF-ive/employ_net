package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.mapper.JobViewMapper;
import com.five.employnet.service.JobViewService;
import com.five.employnet.view.JobView;
import org.springframework.stereotype.Service;

@Service
public class JobViewServiceImpl extends ServiceImpl<JobViewMapper, JobView> implements JobViewService {
}
