package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.entity.Talent;
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

    public TalentController(TalentService talentService) {
        this.talentService = talentService;
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
        String userId = talent.getUser_id();;
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
                .like(major != null && !major.isEmpty(), Talent::getMajor, major)
                .like(home_location != null && !home_location.isEmpty(), Talent::getHome_location, home_location)
                .like(education_level != null && !education_level.isEmpty(), Talent::getEduction_level, education_level)
                .like(sex != null && !sex.isEmpty(), Talent::getSex, sex)
                .like(state != null && !state.isEmpty(), Talent::getState, state)
                .like(job_intention != null && !job_intention.isEmpty(), Talent::getJob_intention, job_intention)
                .like(name != null && !name.isEmpty(), Talent::getName, name);
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
                .like(prompt != null, Talent::getMajor, prompt)
                .or()
                .like(prompt != null, Talent::getName, prompt)
                .or()
                .like(prompt != null, Talent::getSelf_introduce, prompt)
                .or()
                .like(prompt != null, Talent::getHome_location, prompt)
                .or()
                .like(prompt != null, Talent::getEduction_level, prompt)
                .or()
                .like(prompt != null, Talent::getSex, prompt)
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

    @GetMapping
    public R<Talent> getTalent() {
        String userId = BaseContext.getCurrentId();
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
}
