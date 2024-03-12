package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Experience;
import com.five.employnet.mapper.ExperienceMapper;
import com.five.employnet.service.ExperienceService;
import org.springframework.stereotype.Service;

@Service
public class ExperienceServiceImpl extends ServiceImpl<ExperienceMapper, Experience> implements ExperienceService {
}
