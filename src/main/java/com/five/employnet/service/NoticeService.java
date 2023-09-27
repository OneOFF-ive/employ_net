package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Notice;

public interface NoticeService extends IService<Notice> {
    void saveOneNotice(Notice notice);
    void completeNotice(Notice notice);
}
