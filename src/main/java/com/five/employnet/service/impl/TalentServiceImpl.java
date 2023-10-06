package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

        saveDetail(talent, talentId);
    }

    @Override
    public void completeTalent(Talent talent) {
        String talentId = talent.getTalent_id();

        LambdaQueryWrapper<EductionExperience> eductionExperienceQueryWrapper = new LambdaQueryWrapper<>();
        eductionExperienceQueryWrapper.eq(EductionExperience::getTalent_id, talentId);
        List<EductionExperience> eductionExperienceList = eductionExperienceService.list(eductionExperienceQueryWrapper);
        talent.setEduction_experience(eductionExperienceList);

        LambdaQueryWrapper<JobIntention> jobIntentionQueryWrapper = new LambdaQueryWrapper<>();
        jobIntentionQueryWrapper.eq(JobIntention::getTalent_id, talentId);
        List<JobIntention> jobIntentionList = jobIntentionService.list(jobIntentionQueryWrapper);
        talent.setJob_intention(jobIntentionList);

        Experience experience = experienceService.getOneByTalentId(talentId);
        talent.setExperience(experience);
    }

    @Override
    public Talent getTalentWithoutDetailByUserId(String userId) {
        LambdaQueryWrapper<Talent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Talent::getUser_id, userId);
        return this.getOne(queryWrapper);
    }

    @Override
    public Talent getTalentByUserId(String userId) {
        LambdaQueryWrapper<Talent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Talent::getUser_id, userId);
        Talent talent = this.getOne(queryWrapper);
        if (talent != null) {
            completeTalent(talent);
            return talent;
        }
        else return null;
    }

    @Override
    @Transactional
    public Talent update(Talent newTalent) {
        this.updateById(newTalent);
        String talentId = newTalent.getTalent_id();

        LambdaQueryWrapper<EductionExperience> eductionExperienceWrapper = new LambdaQueryWrapper<>();
        eductionExperienceWrapper.eq(EductionExperience::getTalent_id, talentId);
        eductionExperienceService.remove(eductionExperienceWrapper);

        LambdaQueryWrapper<Experience> experienceWrapper = new LambdaQueryWrapper<>();
        experienceWrapper.eq(Experience::getTalent_id, talentId);
        experienceService.remove(experienceWrapper);

        LambdaQueryWrapper<JobIntention> jobIntentionWrapper = new LambdaQueryWrapper<>();
        jobIntentionWrapper.eq(JobIntention::getTalent_id, talentId);
        jobIntentionService.remove(jobIntentionWrapper);


        saveDetail(newTalent, talentId);

        return newTalent;
    }

    private void saveDetail(Talent newTalent, String talentId) {
        List<EductionExperience> eductionExperienceList = newTalent.getEduction_experience();
        for (EductionExperience eductionExperience : eductionExperienceList) {
            eductionExperience.setTalent_id(talentId);
        }
        eductionExperienceService.saveBatch(eductionExperienceList);

        List<JobIntention> jobIntentionList = newTalent.getJob_intention();
        for (JobIntention jobIntention : jobIntentionList) {
            jobIntention.setTalent_id(talentId);
        }
        jobIntentionService.saveBatch(jobIntentionList);

        Experience experience = newTalent.getExperience();
        experience.setTalent_id(talentId);
        experienceService.saveOneExperience(experience);
    }
}
