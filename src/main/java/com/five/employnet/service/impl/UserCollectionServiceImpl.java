package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.UserCollection;
import com.five.employnet.mapper.UserCollectionMapper;
import com.five.employnet.service.UserCollectionService;
import org.springframework.stereotype.Service;

@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {
}
