package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Experience;

public interface ExperienceService extends IService<Experience> {
    public Experience saveOneExperience(Experience experience);
}
