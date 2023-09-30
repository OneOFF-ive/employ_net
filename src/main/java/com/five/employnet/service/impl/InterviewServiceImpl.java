package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.common.BaseContext;
import com.five.employnet.entity.Company;
import com.five.employnet.entity.Interview;
import com.five.employnet.entity.Job;
import com.five.employnet.mapper.InterviewMapper;
import com.five.employnet.service.CompanyService;
import com.five.employnet.service.InterviewService;
import com.five.employnet.service.JobService;
import org.springframework.stereotype.Service;

@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview> implements InterviewService {

    final private CompanyService companyService;
    final private JobService jobService;

    public InterviewServiceImpl(CompanyService companyService, JobService jobService) {
        this.companyService = companyService;
        this.jobService = jobService;
    }

    @Override
    public void completeInterview(Interview interview) {
        String userId = BaseContext.getCurrentId();

        Company company = companyService.getCompanyByUserId(userId);
        interview.setCompany_id(company.getCompany_id());
        interview.setCompany_name(company.getName());
        interview.setCompany_icon(company.getAvatar_url());

        String jobId = interview.getJob_id();
        Job job = jobService.getById(jobId);
        interview.setJob_title(job.getTitle());
    }
}
