package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.mapper.TalentViewMapper;
import com.five.employnet.service.TalentViewService;
import com.five.employnet.view.TalentView;
import org.springframework.stereotype.Service;

@Service
public class TalentViewServiceImpl extends ServiceImpl<TalentViewMapper, TalentView> implements TalentViewService {
}
