package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.EductionExperience;
import com.five.employnet.entity.Experience;
import com.five.employnet.entity.JobIntention;
import com.five.employnet.entity.Talent;
import com.five.employnet.mapper.TalentMapper;
import com.five.employnet.service.EductionExperienceService;
import com.five.employnet.service.ExperienceService;
import com.five.employnet.service.JobIntentionService;
import com.five.employnet.service.TalentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TalentServiceImpl extends ServiceImpl<TalentMapper, Talent> implements TalentService {

    final EductionExperienceService eductionExperienceService;
    final JobIntentionService jobIntentionService;
    final ExperienceService experienceService;

    public TalentServiceImpl(EductionExperienceService eductionExperienceService, JobIntentionService jobIntentionService, ExperienceService experienceService) {
        this.eductionExperienceService = eductionExperienceService;
        this.jobIntentionService = jobIntentionService;
        this.experienceService = experienceService;
    }

    @Override
    @Transactional
    public void saveOneTalent(Talent talent) {
        this.save(talent);
        String talentId = talent.getTalent_id();

        List<EductionExperience> eductionExperienceList = talent.getEduction_experience();
        for (EductionExperience eductionExperience : eductionExperienceList) {
            eductionExperience.setTalent_id(talentId);
        }
        eductionExperienceService.saveBatch(eductionExperienceList);

        List<JobIntention> jobIntentionList = talent.getJob_intention();
        for (JobIntention jobIntention : jobIntentionList) {
            jobIntention.setTalent_id(talentId);
        }
        jobIntentionService.saveBatch(jobIntentionList);

        Experience experience = talent.getExperience();
        experience.setTalent_id(talentId);
        experienceService.saveOneExperience(experience);
    }
}
