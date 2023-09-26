package com.five.employnet.controller;

import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.entity.Talent;
import com.five.employnet.service.TalentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
        String authorizationHeader = request.getHeader("Authorization");
        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
        String userId = jwtUtil.extractUsername(authToken);
        talent.setUser_id(userId);
        talentService.saveOneTalent(talent);
        return R.success(talent);
    }

}
