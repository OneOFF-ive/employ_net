package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.EducationExperience;
import com.five.employnet.entity.Experience;
import com.five.employnet.entity.Talent;
import com.five.employnet.mapper.TalentMapper;
import com.five.employnet.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TalentServiceImpl extends ServiceImpl<TalentMapper, Talent> implements TalentService {

    final EducationExperienceService educationExperienceService;
    final JobIntentionService jobIntentionService;
    final ExperienceService experienceService;

    public TalentServiceImpl(EducationExperienceService educationExperienceService, JobIntentionService jobIntentionService, ExperienceService experienceService) {
        this.educationExperienceService = educationExperienceService;
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

        LambdaQueryWrapper<EducationExperience> eductionExperienceQueryWrapper = new LambdaQueryWrapper<>();
        eductionExperienceQueryWrapper.eq(EducationExperience::getTalent_id, talentId);
        List<EducationExperience> educationExperienceList = educationExperienceService.list(eductionExperienceQueryWrapper);
        talent.setEducation_experience(educationExperienceList);

        LambdaQueryWrapper<Experience> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Experience::getTalent_id, talentId);
        List<Experience> experienceList = experienceService.list(queryWrapper);
        talent.setExperiences(experienceList);
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
        } else return null;
    }

    @Override
    @Transactional
    public Talent update(Talent newTalent) {
        this.updateById(newTalent);
        String talentId = newTalent.getTalent_id();

        LambdaQueryWrapper<EducationExperience> eductionExperienceWrapper = new LambdaQueryWrapper<>();
        eductionExperienceWrapper.eq(EducationExperience::getTalent_id, talentId);
        educationExperienceService.remove(eductionExperienceWrapper);

        LambdaQueryWrapper<Experience> experienceWrapper = new LambdaQueryWrapper<>();
        experienceWrapper.eq(Experience::getTalent_id, talentId);
        experienceService.remove(experienceWrapper);

        saveDetail(newTalent, talentId);

        return newTalent;
    }

    private void saveDetail(Talent newTalent, String talentId) {
        List<EducationExperience> educationExperienceList = newTalent.getEducation_experience();
        if (educationExperienceList != null) {
            for (EducationExperience educationExperience : educationExperienceList) {
                educationExperience.setTalent_id(talentId);
            }
            educationExperienceService.saveBatch(educationExperienceList);
        }

        List<Experience> experienceList = newTalent.getExperiences();
        if (experienceList != null) {
            for (Experience experience : experienceList) {
                experience.setTalent_id(talentId);
            }
            experienceService.saveBatch(experienceList);
        }
    }
}
