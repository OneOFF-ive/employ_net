package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.entity.EductionExperience;
import com.five.employnet.entity.Experience;
import com.five.employnet.entity.JobIntention;
import com.five.employnet.entity.Talent;
import com.five.employnet.service.EductionExperienceService;
import com.five.employnet.service.ExperienceService;
import com.five.employnet.service.JobIntentionService;
import com.five.employnet.service.TalentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/talent")
public class TalentController {

    final private TalentService talentService;
    final private JwtUtil jwtUtil;
    final private EductionExperienceService eductionExperienceService;
    final private JobIntentionService jobIntentionService;
    final private ExperienceService experienceService;

    public TalentController(TalentService talentService, JwtUtil jwtUtil, EductionExperienceService eductionExperienceService, JobIntentionService jobIntentionService, ExperienceService experienceService) {
        this.talentService = talentService;
        this.jwtUtil = jwtUtil;
        this.eductionExperienceService = eductionExperienceService;
        this.jobIntentionService = jobIntentionService;
        this.experienceService = experienceService;
    }


    @PostMapping
    public R<Talent> save(HttpServletRequest request, @RequestBody Talent talent) {
        String authorizationHeader = request.getHeader("Authorization");
        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
        String userId = jwtUtil.extractUsername(authToken);
        talent.setUser_id(userId);
        talentService.saveOneTalent(talent);
        return R.success(talent);
    }

    @GetMapping("/page")
    public R<Page<Talent>> getPage(@RequestParam int page, @RequestParam int pageSize, String prompt) {
        Page<Talent> talentPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Talent> talentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        talentLambdaQueryWrapper.like(prompt != null, Talent::getSelf_introduce, prompt);
        talentService.page(talentPage, talentLambdaQueryWrapper);
        List<Talent> talentList = talentPage.getRecords();

        for (Talent talent: talentList) {
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

        return R.success(talentPage);
    }

}
