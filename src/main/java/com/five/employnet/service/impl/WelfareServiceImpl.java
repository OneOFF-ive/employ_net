package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Welfare;
import com.five.employnet.mapper.WelfareMapper;
import com.five.employnet.service.WelfareService;
import org.springframework.stereotype.Service;

@Service
public class WelfareServiceImpl extends ServiceImpl<WelfareMapper, Welfare> implements WelfareService {
}
