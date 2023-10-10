package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.mapper.InterviewViewMapper;
import com.five.employnet.service.InterviewViewService;
import com.five.employnet.view.InterviewView;
import org.springframework.stereotype.Service;

@Service
public class InterviewViewServiceImpl extends ServiceImpl<InterviewViewMapper, InterviewView> implements InterviewViewService {
}
