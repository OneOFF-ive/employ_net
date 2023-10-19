package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Visitor;

public interface VisitorService extends IService<Visitor> {
    void saveVisitorRecordsToDatabase();
    void addVisitor(Visitor visitor);
    void scheduleSaveVisitorRecordsToDatabase();
}
