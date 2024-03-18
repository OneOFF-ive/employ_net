package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.R;
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
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/interview")
//@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
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
    public R<List<InterviewView>> getTalentInterview(@RequestParam("status") String status) {
        String userId = BaseContext.getCurrentId();
        Talent talent = talentService.getTalentWithoutDetailByUserId(userId);
        if (talent != null) {
            LambdaQueryWrapper<InterviewView> queryWrapper = filterTalentInterview(status, talent);
            List<InterviewView> interviewViewList = interviewViewService.list(queryWrapper);
            return R.success(interviewViewList);
        } else {
            return R.success(new ArrayList<>());
        }
    }

    @GetMapping("/talent/count")
    public R<Integer> getTalentInterviewCount(@RequestParam("status") String status) {
        String userId = BaseContext.getCurrentId();
        Talent talent = talentService.getTalentWithoutDetailByUserId(userId);
        if (talent != null) {
            LambdaQueryWrapper<InterviewView> queryWrapper = filterTalentInterview(status, talent);
            int count = interviewViewService.count(queryWrapper);
            return R.success(count);
        } else {
            return R.success(0);
        }
    }

    private LambdaQueryWrapper<InterviewView> filterTalentInterview(@RequestParam("status") String status, Talent talent) {
        String talentId = talent.getTalent_id();
        LambdaQueryWrapper<InterviewView> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.equals(status, "待面试")) {
            queryWrapper.eq(Interview::getTalent_id, talentId)
                    .eq(Interview::getStatus, "待面试");
        } else {
            queryWrapper.eq(Interview::getTalent_id, talentId)
                    .ne(Interview::getStatus, "待面试");
        }
        return queryWrapper;
    }

    @GetMapping("/company")
    public R<List<InterviewView>> getCompanyInterview(@RequestParam("status") String status) {
        String userId = BaseContext.getCurrentId();
        Company company = companyService.getCompanyByUserId(userId);
        if (company != null) {
            LambdaQueryWrapper<InterviewView> queryWrapper = filterCompanyInterview(status, company);

            List<InterviewView> interviewViewList = interviewViewService.list(queryWrapper);
            return R.success(interviewViewList);
        } else {
            return R.success(new ArrayList<>());
        }
    }

    private LambdaQueryWrapper<InterviewView> filterCompanyInterview(@RequestParam("status") String status, Company company) {
        String companyId = company.getCompany_id();
        LambdaQueryWrapper<InterviewView> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Interview::getCompany_id, companyId);
        if (Objects.equals(status, "待面试")) {
            queryWrapper.eq(Interview::getStatus, "待面试");
        } else {
            queryWrapper.eq(Interview::getStatus, "待处理");
        }
        return queryWrapper;
    }

    @GetMapping("/company/count")
    public R<Integer> getCompanyInterviewCount(@RequestParam("status") String status) {
        String userId = BaseContext.getCurrentId();
        Company company = companyService.getCompanyByUserId(userId);
        if (company != null) {
            LambdaQueryWrapper<InterviewView> queryWrapper = filterCompanyInterview(status, company);
            int count = interviewViewService.count(queryWrapper);
            return R.success(count);
        } else {
            return R.success(0);
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

    @GetMapping("/refuse")
    public R<String> refuse(@RequestParam("id") String id) {
        UpdateWrapper<Interview> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("status", "已拒绝");
        interviewService.update(updateWrapper);
        return R.success("成功");
    }

    @PostMapping("/accept")
    public R<String> refuse(@RequestBody Interview interview) {
        UpdateWrapper<Interview> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", interview.getId())
                .set("status", "待面试")
                .set("info", interview.getInfo())
                .set("address", interview.getAddress())
                .set("interview_time", interview.getInterview_time());
        interviewService.update(updateWrapper);
        return R.success("成功");
    }
}
