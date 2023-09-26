package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Experience;

public interface ExperienceService extends IService<Experience> {
    void saveOneExperience(Experience experience);
    Experience getOneByTalentId(String talentID);
}
