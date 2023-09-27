package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Notice;
import com.five.employnet.entity.NoticeMessage;
import com.five.employnet.mapper.NoticeMapper;
import com.five.employnet.service.NoticeMessageService;
import com.five.employnet.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    final private NoticeMessageService noticeMessageService;

    public NoticeServiceImpl(NoticeMessageService noticeMessageService) {
        this.noticeMessageService = noticeMessageService;
    }

    @Override
    @Transactional
    public void saveOneNotice(Notice notice) {
        this.save(notice);
        String noticeId = notice.getLid();
        List<NoticeMessage> noticeMessageList = notice.getMessage_list();
        for (NoticeMessage message: noticeMessageList) {
            message.setNotice_id(noticeId);
        }
        noticeMessageService.saveBatch(noticeMessageList);
    }

    @Override
    public void completeNotice(Notice notice) {
        String noticeId = notice.getLid();
        LambdaQueryWrapper<NoticeMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NoticeMessage::getNotice_id, noticeId);
        List<NoticeMessage> noticeMessageList = noticeMessageService.list(queryWrapper);
        notice.setMessage_list(noticeMessageList);
    }
}
