package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Experience;
import com.five.employnet.entity.ExperienceDetail;
import com.five.employnet.mapper.ExperienceMapper;
import com.five.employnet.service.ExperienceDetailService;
import com.five.employnet.service.ExperienceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceServiceImpl extends ServiceImpl<ExperienceMapper, Experience> implements ExperienceService {

    final ExperienceDetailService experienceDetailService;

    public ExperienceServiceImpl(ExperienceDetailService experienceDetailService) {
        this.experienceDetailService = experienceDetailService;
    }

    @Override
    public void saveOneExperience(Experience experience) {
        this.save(experience);
        String experienceId = experience.getExperience_id();
        List<ExperienceDetail> experienceDetailList = experience.getList();
        for (ExperienceDetail experienceDetail: experienceDetailList) {
            experienceDetail.setExperience_id(experienceId);
        }
        experienceDetailService.saveBatch(experienceDetailList);
    }

    @Override
    public Experience getOneByTalentId(String talentID) {
        LambdaQueryWrapper<Experience> experienceQueryWrapper = new LambdaQueryWrapper<>();
        experienceQueryWrapper.eq(Experience::getTalent_id, talentID);
        Experience experience = this.getOne(experienceQueryWrapper);

        if (experience != null) {
            String experienceID = experience.getExperience_id();
            LambdaQueryWrapper<ExperienceDetail> experienceDetailQueryWrapper = new LambdaQueryWrapper<>();
            experienceDetailQueryWrapper.eq(ExperienceDetail::getExperience_id, experienceID);
            List<ExperienceDetail> experienceDetailList = experienceDetailService.list(experienceDetailQueryWrapper);

            experience.setList(experienceDetailList);
        }

        return experience;
    }
}
