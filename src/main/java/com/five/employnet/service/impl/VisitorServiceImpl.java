package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Visitor;
import com.five.employnet.mapper.VisitorMapper;
import com.five.employnet.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {
    private final ScheduledExecutorService executorService;
    private final List<Visitor> visitorList;

    public VisitorServiceImpl(ScheduledExecutorService executorService) {
        this.executorService = executorService;
        visitorList = new ArrayList<>();
    }

    @Override
    public void scheduleSaveVisitorRecordsToDatabase() {
        executorService.scheduleWithFixedDelay(this::saveVisitorRecordsToDatabase, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    @Transactional
    public void saveVisitorRecordsToDatabase() {
        log.info("saving.............................");
        // 将内存中的访客记录写入数据库
        if (!visitorList.isEmpty()) {
            this.saveBatch(visitorList);
            visitorList.clear();
        }
    }

    @Override
    public void addVisitor(Visitor visitor) {
        visitorList.add(visitor);
    }
}
