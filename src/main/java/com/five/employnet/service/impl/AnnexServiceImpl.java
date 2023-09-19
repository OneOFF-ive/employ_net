package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Annex;
import com.five.employnet.mapper.AnnexMapper;
import com.five.employnet.service.AnnexService;
import org.springframework.stereotype.Service;

@Service
public class AnnexServiceImpl extends ServiceImpl<AnnexMapper, Annex> implements AnnexService {
}
