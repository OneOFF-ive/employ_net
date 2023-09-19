package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.entity.Company;
import com.five.employnet.entity.User;
import com.five.employnet.service.CompanyService;
import com.five.employnet.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;
    private final WeChatService weChatService;
    private final JwtUtil jwtUtil;

    public CompanyController(CompanyService companyService, WeChatService weChatService, JwtUtil jwtUtil) {
        this.companyService = companyService;
        this.weChatService = weChatService;
        this.jwtUtil = jwtUtil;
    }

    @RequestMapping("/login")
    public R<Company> login(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");

        log.info(code);

        JSONObject responseBody = weChatService.getWeChatSessionInfo(code);

        if (responseBody.has("errcode")) {
            return R.error("微信授权失败");
        } else {
            String openId = (String) responseBody.get("openid");
            String sessionKey = (String) responseBody.get("session_key");

            LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Company::getOpenId, openId);
            Company company = companyService.getOne(queryWrapper);
            if (company == null) {
                company = new Company();
                company.setOpenId(openId);
                company.setSessionKey(sessionKey);
                log.info(company.toString());
                companyService.save(company);
            }
            company.setOpenId("");
            company.setSessionKey("");
            R<Company> res = R.success(company);

            Long companyId = company.getId();
            String token = jwtUtil.generateToken(String.valueOf(companyId));
            res.add("token", token);
            return res;
        }
    }

    @PostMapping("/update")
    public R<String> update(HttpServletRequest request, @RequestBody Company company) {
        String authorizationHeader = request.getHeader("Authorization");
        String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀
        Long companyId = Long.valueOf(jwtUtil.extractUsername(authToken));
        company.setId(companyId);
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Company::getId, companyId);
        companyService.updateById(company);
        return R.success("success");
    }
}
