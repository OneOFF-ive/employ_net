package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Company;

public interface CompanyService extends IService<Company> {
    Company getCompanyByUserId(String userId);
}
