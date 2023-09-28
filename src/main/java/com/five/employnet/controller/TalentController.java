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
    final private JwtUtil jwtUtil;

    public TalentController(TalentService talentService, JwtUtil jwtUtil) {
        this.talentService = talentService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping
    public R<Talent> save(HttpServletRequest request, @RequestBody Talent talent) {
//        String authorizationHeader = request.getHeader("Authorization");
//        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
//        String userId = jwtUtil.extractUserId(authToken);
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

        for (Talent talent: talentList) {
            talentService.completeTalent(talent);
        }

        return R.success(talentPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam String talentId) {
        talentService.removeById(talentId);
        return R.success("1");
    }

}
