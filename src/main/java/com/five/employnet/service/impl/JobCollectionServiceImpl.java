package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.JobCollection;
import com.five.employnet.mapper.JobCollectionMapper;
import com.five.employnet.service.JobCollectionService;
import org.springframework.stereotype.Service;

@Service
public class JobCollectionServiceImpl extends ServiceImpl<JobCollectionMapper, JobCollection> implements JobCollectionService {
}
