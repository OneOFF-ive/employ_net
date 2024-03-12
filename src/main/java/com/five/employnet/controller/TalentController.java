package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.entity.EducationExperience;
import com.five.employnet.entity.Experience;
import com.five.employnet.entity.Talent;
import com.five.employnet.service.EducationExperienceService;
import com.five.employnet.service.ExperienceService;
import com.five.employnet.service.TalentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/talent")
//@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class TalentController {

    final private TalentService talentService;
    final private ExperienceService experienceService;
    final private EducationExperienceService educationExperienceService;

    public TalentController(TalentService talentService, ExperienceService experienceService, EducationExperienceService educationExperienceService) {
        this.talentService = talentService;
        this.experienceService = experienceService;
        this.educationExperienceService = educationExperienceService;
    }


    @PostMapping
    public R<Talent> save(@RequestBody Talent talent) {
        String userId = BaseContext.getCurrentId();
        talent.setUser_id(userId);
        talentService.saveOneTalent(talent);
        return R.success(talent);
    }

    @PostMapping("/save")
    public R<Talent> saveOne(@RequestBody Talent talent) {
        String userId = talent.getUser_id();
        ;
        Talent t = talentService.getTalentByUserId(userId);
        if (t == null) {
            talentService.saveOneTalent(talent);
            return R.success(talent);
        }
        return R.error("该用户已有简历信息");
    }

    @GetMapping("/page")
    public R<Page<Talent>> getPage(@RequestParam int page, @RequestParam int pageSize,
                                   String home_location,
                                   String education_level,
                                   String sex,
                                   String state,
                                   String job_intention,
                                   String major,
                                   String name) {
        Page<Talent> talentPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Talent> talentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        talentLambdaQueryWrapper
                .like(state != null && !state.isEmpty(), Talent::getState, state)
                .like(job_intention != null && !job_intention.isEmpty(), Talent::getJob_intention, job_intention);
        talentService.page(talentPage, talentLambdaQueryWrapper);
        List<Talent> talentList = talentPage.getRecords();
//
//        for (Talent talent : talentList) {
//            talentService.completeTalent(talent);
//        }

        return R.success(talentPage);
    }

    @GetMapping("/search")
    public R<Page<Talent>> search(@RequestParam int page, @RequestParam int pageSize, String prompt) {
        Page<Talent> talentPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Talent> talentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        talentLambdaQueryWrapper
                .like(prompt != null, Talent::getSelf_introduce, prompt)
                .or()
                .like(prompt != null, Talent::getState, prompt)
                .or()
                .like(prompt != null, Talent::getJob_intention, prompt);
        talentService.page(talentPage, talentLambdaQueryWrapper);
        List<Talent> talentList = talentPage.getRecords();
//
//        for (Talent talent : talentList) {
//            talentService.completeTalent(talent);
//        }

        return R.success(talentPage);
    }

    @DeleteMapping
    public R<String> delete() {
        String userId = BaseContext.getCurrentId();
        Talent talent = talentService.getTalentByUserId(userId);
        String talentId = talent.getTalent_id();
        talentService.removeById(talentId);
        return R.success("1");
    }

    @PostMapping("/delete")
    public R<String> deleteByIds(@RequestBody Map<String, List<String>> requestBody) {
        List<String> ids = requestBody.get("ids");
        talentService.removeByIds(ids);
        return R.success("删除失败");
    }

    @PostMapping("/update")
    public R<Talent> updateById(@RequestBody Talent talent) {
        talentService.updateById(talent);
        return R.success(talent);
    }

    @PostMapping("/update/intention")
    public R<Talent> updateIntentionById(@RequestBody Talent talent) {
        UpdateWrapper<Talent> wrapper = new UpdateWrapper<>();
        wrapper.eq("talent_id", talent.getTalent_id()).set("job_intention", talent.getJob_intention());
        talentService.update(wrapper);
        return R.success(talent);
    }

    @PostMapping("/update/introduction")
    public R<Talent> updateIntroductionById(@RequestBody Talent talent) {
        UpdateWrapper<Talent> wrapper = new UpdateWrapper<>();
        wrapper.eq("talent_id", talent.getTalent_id()).set("self_introduce", talent.getSelf_introduce());
        talentService.update(wrapper);
        return R.success(talent);
    }

    @PostMapping("/update/state")
    public R<Talent> updateStateById(@RequestBody Talent talent) {
        UpdateWrapper<Talent> wrapper = new UpdateWrapper<>();
        wrapper.eq("talent_id", talent.getTalent_id()).set("state", talent.getState());
        talentService.update(wrapper);
        return R.success(talent);
    }

    @GetMapping
    public R<Talent> getTalent() {
        String userId = BaseContext.getCurrentId();
        Talent talent = talentService.getTalentByUserId(userId);
        return R.success(talent);
    }

    @GetMapping("/by/userId")
    public R<Talent> getTalentByUserId(@RequestParam("userId") String userId) {
        Talent talent = talentService.getTalentByUserId(userId);
        return R.success(talent);
    }

    @GetMapping("get/detail")
    public R<Talent> getDetail(@RequestParam("id") String id) {
        Talent talent = talentService.getById(id);
        if (talent != null) {
            talentService.completeTalent(talent);
            return R.success(talent);
        }
        return R.error("信息不存在");
    }

    @PutMapping
    public R<Talent> updateOrSave(@RequestBody Talent newTalent) {
        String userId = BaseContext.getCurrentId();
        Talent talent = talentService.getTalentByUserId(userId);
        if (talent == null) {
            newTalent.setUser_id(userId);
            talentService.saveOneTalent(newTalent);
            return R.success(newTalent);
        } else {
            newTalent.setTalent_id(talent.getTalent_id());
            return R.success(talentService.update(newTalent));
        }
    }

    @GetMapping("/count")
    public R<Integer> number() {
        int count = talentService.count();
        return R.success(count);
    }

    @PostMapping("/add/experience")
    public R<String> addExperience(@RequestBody Experience experience) {
        String userId = BaseContext.getCurrentId();
        Talent talent = talentService.getTalentByUserId(userId);
        experience.setTalent_id(talent.getTalent_id());
        experienceService.save(experience);
        return R.success("保存成功");
    }

    @PostMapping("/update/experience")
    public R<String> updateExperience(@RequestBody Experience experience) {
        experienceService.updateById(experience);
        return R.success("修改成功");
    }

    @DeleteMapping("/delete/experience")
    public R<String> deleteExperience(@RequestParam("id") String id) {
        experienceService.removeById(id);
        return R.success("删除成功");
    }

    @PostMapping("/add/education_experience")
    public R<String> addEducationExperience(@RequestBody EducationExperience experience) {
        String userId = BaseContext.getCurrentId();
        Talent talent = talentService.getTalentByUserId(userId);
        experience.setTalent_id(talent.getTalent_id());
        educationExperienceService.save(experience);
        return R.success("保存成功");
    }

    @PostMapping("/update/education_experience")
    public R<String> updateEducationExperience(@RequestBody EducationExperience experience) {
        educationExperienceService.updateById(experience);
        return R.success("修改成功");
    }

    @DeleteMapping("/delete/education_experience")
    public R<String> deleteEducationExperience(@RequestParam("id") String id) {
        educationExperienceService.removeById(id);
        return R.success("删除成功");
    }
}
