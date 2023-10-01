package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.entity.Talent;
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

    @GetMapping("/page")
    public R<Page<Talent>> getPage(@RequestParam int page, @RequestParam int pageSize, String prompt) {
        Page<Talent> talentPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Talent> talentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        talentLambdaQueryWrapper.like(prompt != null, Talent::getSelf_introduce, prompt);
        talentService.page(talentPage, talentLambdaQueryWrapper);
        List<Talent> talentList = talentPage.getRecords();

        for (Talent talent : talentList) {
            talentService.completeTalent(talent);
        }

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

    @GetMapping
    public R<Talent> getTalent() {
        String userId = BaseContext.getCurrentId();
        Talent talent = talentService.getTalentByUserId(userId);
        return R.success(talent);
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
