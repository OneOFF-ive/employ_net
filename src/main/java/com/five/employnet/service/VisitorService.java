package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.Visitor;

import java.time.LocalDate;

public interface VisitorService extends IService<Visitor> {
    void saveVisitorRecordsToDatabase();
    void addVisitor(Visitor visitor);
    void scheduleSaveVisitorRecordsToDatabase();

    int getNumberAt(LocalDate localDate);
    int getViewAt(LocalDate localDate);
}
