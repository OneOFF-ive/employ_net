package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.TalentCollection;
import com.five.employnet.mapper.TalentCollectionMapper;
import com.five.employnet.service.TalentCollectionService;
import org.springframework.stereotype.Service;

@Service
public class TalentCollectionServiceImpl extends ServiceImpl<TalentCollectionMapper, TalentCollection> implements TalentCollectionService {
}
