package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.NoticeMessage;
import com.five.employnet.mapper.NoticeMessageMapper;
import com.five.employnet.service.NoticeMessageService;
import org.springframework.stereotype.Service;

@Service
public class NoticeMessageServiceImpl extends ServiceImpl<NoticeMessageMapper, NoticeMessage> implements NoticeMessageService {
}
