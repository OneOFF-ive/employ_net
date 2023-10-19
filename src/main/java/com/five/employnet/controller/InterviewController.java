package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.entity.Company;
import com.five.employnet.entity.Interview;
import com.five.employnet.entity.Talent;
import com.five.employnet.service.CompanyService;
import com.five.employnet.service.InterviewService;
import com.five.employnet.service.InterviewViewService;
import com.five.employnet.service.TalentService;
import com.five.employnet.view.InterviewView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/interview")
@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class InterviewController {
    final private InterviewService interviewService;
    final private InterviewViewService interviewViewService;
    final private TalentService talentService;
    final private CompanyService companyService;


    public InterviewController(InterviewService interviewService, InterviewViewService interviewViewService, TalentService talentService, CompanyService companyService) {
        this.interviewService = interviewService;
        this.interviewViewService = interviewViewService;
        this.talentService = talentService;
        this.companyService = companyService;
    }

    @GetMapping("/talent")
    public R<List<InterviewView>> getTalentInterview(@RequestParam("receive") boolean receive) {
        String userId = BaseContext.getCurrentId();
        Talent talent = talentService.getTalentWithoutDetailByUserId(userId);
        if (talent != null) {
            String talentId = talent.getTalent_id();
            LambdaQueryWrapper<InterviewView> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(Interview::getTalent_id, talentId)
                    .eq(Interview::isReceive, receive);
            List<InterviewView> interviewViewList = interviewViewService.list(queryWrapper);
            return R.success(interviewViewList);
        } else {
            return R.success(new ArrayList<>());
        }
    }

    @GetMapping("/company")
    public R<List<InterviewView>> getCompanyInterview(@RequestParam("receive") boolean receive) {
        String userId = BaseContext.getCurrentId();
        Company company = companyService.getCompanyByUserId(userId);
        if (company != null) {
            String companyId = company.getCompany_id();
            LambdaQueryWrapper<InterviewView> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper
                    .eq(Interview::getCompany_id, companyId)
                    .eq(Interview::isReceive, receive);
            List<InterviewView> interviewViewList = interviewViewService.list(queryWrapper);
            return R.success(interviewViewList);
        } else {
            return R.success(new ArrayList<>());
        }
    }

    @PostMapping
    public R<Interview> saveInterview(@RequestBody Interview interview) {
        interviewService.completeInterview(interview);
        interviewService.save(interview);
        return R.success(interview);
    }

    @PutMapping
    @Transactional
    public R<Interview> updateInterview(@RequestBody Interview interview) {
        interviewService.completeInterview(interview);
        interviewService.updateById(interview);
        return R.success(interview);
    }

    @DeleteMapping
    public R<String> deleteInterview(@RequestParam("id") String id) {
        interviewService.removeById(id);
        return R.success("删除成功");
    }
}
