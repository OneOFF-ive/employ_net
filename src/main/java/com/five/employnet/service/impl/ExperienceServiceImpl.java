package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Experience;
import com.five.employnet.entity.ExperienceDetail;
import com.five.employnet.mapper.ExperienceMapper;
import com.five.employnet.service.ExperienceDetailService;
import com.five.employnet.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceServiceImpl extends ServiceImpl<ExperienceMapper, Experience> implements ExperienceService {

    final ExperienceDetailService experienceDetailService;

    public ExperienceServiceImpl(ExperienceDetailService experienceDetailService) {
        this.experienceDetailService = experienceDetailService;
    }

    @Override
    public Experience saveOneExperience(Experience experience) {
        this.save(experience);
        String experienceId = experience.getExperience_id();
        List<ExperienceDetail> experienceDetailList = experience.getList();
        for (ExperienceDetail experienceDetail: experienceDetailList) {
            experienceDetail.setExperience_id(experienceId);
        }
        experienceDetailService.saveBatch(experienceDetailList);
        return experience;
    }
}
