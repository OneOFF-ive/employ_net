package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Interview;

import java.util.List;

public interface InterviewService extends IService<Interview> {
    void completeInterview(Interview interview);
}
