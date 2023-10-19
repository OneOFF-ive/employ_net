package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.BaseContext;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.entity.Company;
import com.five.employnet.service.CompanyService;
import com.five.employnet.service.WeChatService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/company")
@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
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
            queryWrapper.eq(Company::getOpen_id, openId);
            Company company = companyService.getOne(queryWrapper);
            if (company == null) {
                company = new Company();
                company.setOpen_id(openId);
                company.setSession_key(sessionKey);
                log.info(company.toString());
                companyService.save(company);
            }
            company.setOpen_id("");
            company.setSession_key("");
            R<Company> res = R.success(company);

            String companyId = company.getCompany_id();
            String token = jwtUtil.generateToken(String.valueOf(companyId));
            res.add("token", token);
            return res;
        }
    }

    @PutMapping
    public R<Company> updateOrSave(@RequestBody Company newCompany) {
        String userId = BaseContext.getCurrentId();
        Company company = companyService.getCompanyByUserId(userId);
        if (company == null) {
            newCompany.setUser_id(userId);
            companyService.save(newCompany);
        }
        else {
            String companyId = company.getCompany_id();
            newCompany.setCompany_id(companyId);
            companyService.updateById(newCompany);
        }
        return R.success(newCompany);
    }

    @GetMapping
    public R<Company> getCompany() {
        String userId = BaseContext.getCurrentId();
        Company company = companyService.getCompanyByUserId(userId);
        if (company != null) return R.success(company);
        else return R.error(null);
    }

    @DeleteMapping
    public R<String> delete() {
        String userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Company::getUser_id, userId);
        companyService.remove(queryWrapper);
        return R.success("删除成功");
    }

    @GetMapping("/all")
    public R<Page<Company>> all(@RequestParam int page, @RequestParam int pageSize) {
        Page<Company> companyPage = new Page<>(page, pageSize);
        companyService.page(companyPage);
        return R.success(companyPage);
    }

    @PostMapping("/save")
    public R<Company> save(@RequestBody Company company) {
        companyService.save(company);
        return R.success(company);
    }

    @PostMapping("/update")
    public R<Company> update(@RequestBody Company company) {
        companyService.updateById(company);
        return R.success(company);
    }

    @DeleteMapping("/delete")
    public R<String> deleteByIds(@RequestParam List<String> ids) {
        companyService.removeByIds(ids);
        return R.success("删除成功");
    }

    @GetMapping("/count")
    public R<Integer> count() {
        int count = companyService.count();
        return R.success(count);
    }

}
