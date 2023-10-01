package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.*;
import com.five.employnet.mapper.UserMapper;
import com.five.employnet.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    final private ApplicationService applicationService;
    final private CompanyService companyService;
    final private JobCollectionService jobCollectionService;
    final private TalentService talentService;
    final private TalentCollectionService talentCollectionService;

    public UserServiceImpl(ApplicationService applicationService, CompanyService companyService, JobCollectionService jobCollectionService, TalentService talentService, TalentCollectionService talentCollectionService) {
        this.applicationService = applicationService;
        this.companyService = companyService;
        this.jobCollectionService = jobCollectionService;
        this.talentService = talentService;
        this.talentCollectionService = talentCollectionService;
    }

    @Override
    @Transactional
    public boolean transfer(User sourceUser, User targetUser) {
        String sourceUserId = sourceUser.getUser_id();
        String targetUserId = targetUser.getUser_id();

        try {
            UpdateWrapper<Application> applicationUpdateWrapper = new UpdateWrapper<>();
            applicationUpdateWrapper.eq("user_id", sourceUserId);
            Application application = new Application();
            application.setUser_id(targetUserId);
            applicationService.update(application, applicationUpdateWrapper);
            LambdaQueryWrapper<Application> applicationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            applicationLambdaQueryWrapper.eq(Application::getUser_id, sourceUserId);
            applicationService.remove(applicationLambdaQueryWrapper);

            UpdateWrapper<Company> companyUpdateWrapper = new UpdateWrapper<>();
            companyUpdateWrapper.eq("user_id", sourceUserId);
            Company company = new Company();
            company.setUser_id(targetUserId);
            companyService.update(company, companyUpdateWrapper);
            LambdaQueryWrapper<Company> companyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            companyLambdaQueryWrapper.eq(Company::getUser_id, sourceUserId);
            companyService.remove(companyLambdaQueryWrapper);

            UpdateWrapper<JobCollection> jobCollectionUpdateWrapper = new UpdateWrapper<>();
            jobCollectionUpdateWrapper.eq("user_id", sourceUserId);
            JobCollection jobCollection = new JobCollection();
            jobCollection.setUser_id(targetUserId);
            jobCollectionService.update(jobCollection, jobCollectionUpdateWrapper);
            LambdaQueryWrapper<JobCollection> jobCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            jobCollectionLambdaQueryWrapper.eq(JobCollection::getUser_id, sourceUserId);
            jobCollectionService.remove(jobCollectionLambdaQueryWrapper);

            UpdateWrapper<Talent> talentUpdateWrapper = new UpdateWrapper<>();
            talentUpdateWrapper.eq("user_id", sourceUserId);
            Talent talent = new Talent();
            talent.setUser_id(targetUserId);
            talentService.update(talent, talentUpdateWrapper);
            LambdaQueryWrapper<Talent> talentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            talentLambdaQueryWrapper.eq(Talent::getUser_id, sourceUserId);
            talentService.remove(talentLambdaQueryWrapper);

            UpdateWrapper<TalentCollection> talentCollectionUpdateWrapper = new UpdateWrapper<>();
            talentCollectionUpdateWrapper.eq("user_id", sourceUserId);
            TalentCollection talentCollection = new TalentCollection();
            talentCollection.setUser_id(targetUserId);
            talentCollectionService.update(talentCollection, talentCollectionUpdateWrapper);
            LambdaQueryWrapper<TalentCollection> talentCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            talentCollectionLambdaQueryWrapper.eq(TalentCollection::getUser_id, sourceUserId);
            talentCollectionService.remove(talentCollectionLambdaQueryWrapper);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
