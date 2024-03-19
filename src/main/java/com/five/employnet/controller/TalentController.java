package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.R;
import com.five.employnet.entity.EducationExperience;
import com.five.employnet.entity.Experience;
import com.five.employnet.entity.Talent;
import com.five.employnet.service.EducationExperienceService;
import com.five.employnet.service.ExperienceService;
import com.five.employnet.service.TalentService;
import com.five.employnet.service.TalentViewService;
import com.five.employnet.view.TalentView;
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
    final private TalentViewService talentViewService;
    final private ExperienceService experienceService;
    final private EducationExperienceService educationExperienceService;

    public TalentController(TalentService talentService, TalentViewService talentViewService, ExperienceService experienceService, EducationExperienceService educationExperienceService) {
        this.talentService = talentService;
        this.talentViewService = talentViewService;
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
        Talent t = talentService.getTalentByUserId(userId);
        if (t == null) {
            talentService.saveOneTalent(talent);
            return R.success(talent);
        }
        return R.error("该用户已有简历信息");
    }

    @GetMapping("/page")
    public R<Page<TalentView>> getPage(@RequestParam int page, @RequestParam int pageSize,
                                       String state,
                                       String job_intention) {
        Page<TalentView> talentPage = new Page<>(page, pageSize);
        QueryWrapper<TalentView> talentLambdaQueryWrapper = new QueryWrapper<>();
        talentLambdaQueryWrapper
                .like(state != null && !state.isEmpty(), "state", state)
                .like(job_intention != null && !job_intention.isEmpty(), "job_intention", job_intention)
                .orderBy(true, false, "RAND()");
        talentViewService.page(talentPage, talentLambdaQueryWrapper);
        return R.success(talentPage);
    }

    @GetMapping("/search")
    public R<Page<TalentView>> search(@RequestParam int page, @RequestParam int pageSize, String prompt) {
        Page<TalentView> talentPage = new Page<>(page, pageSize);
        QueryWrapper<TalentView> talentLambdaQueryWrapper = new QueryWrapper<>();
        talentLambdaQueryWrapper
                .like(prompt != null, "self_introduce", prompt)
                .or()
                .like(prompt != null, "state", prompt)
                .or()
                .like(prompt != null, "job_intention", prompt)
                .orderBy(true, false, "RAND()");
        talentViewService.page(talentPage, talentLambdaQueryWrapper);

        return R.success(talentPage);
    }

    @GetMapping("/latest")
    public R<Page<TalentView>> getLatest(@RequestParam int page, @RequestParam int pageSize) {
        Page<TalentView> talentPage = new Page<>(page, pageSize);
        QueryWrapper<TalentView> talentLambdaQueryWrapper = new QueryWrapper<>();
        talentLambdaQueryWrapper
                .orderBy(true, false, "update_time");
        talentViewService.page(talentPage, talentLambdaQueryWrapper);
        return R.success(talentPage);
    }

    @GetMapping("/near")
    public R<Page<TalentView>> getLatest(@RequestParam int page, @RequestParam int pageSize, @RequestParam("city") String city) {
        Page<TalentView> talentPage = new Page<>(page, pageSize);
        QueryWrapper<TalentView> talentLambdaQueryWrapper = new QueryWrapper<>();
        talentLambdaQueryWrapper
                .like("location", city)
                .orderBy(true, false, "update_time");
        talentViewService.page(talentPage, talentLambdaQueryWrapper);
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

    @GetMapping("/view")
    public R<Talent> getTalentView(@RequestParam("id") String id) {
        TalentView talent = talentViewService.getById(id);
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
