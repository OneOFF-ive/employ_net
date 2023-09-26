package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.common.R;
import com.five.employnet.entity.Talent;

public interface TalentService extends IService<Talent> {
    public Talent saveOneTalent(Talent talent);
}
