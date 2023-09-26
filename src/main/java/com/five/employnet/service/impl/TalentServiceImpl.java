package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Talent;
import com.five.employnet.mapper.TalentMapper;
import com.five.employnet.service.TalentService;
import org.springframework.stereotype.Service;

@Service
public class TalentServiceImpl extends ServiceImpl<TalentMapper, Talent> implements TalentService {
}
