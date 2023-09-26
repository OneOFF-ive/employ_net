package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Job implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private String jobId;
    private String companyId;
    private String title;
    private String salary;
    private LocalDate time;
    private int number;
    private String lab;
    private String jobLab;
    private String business;
    private String company;
    private String avatarUrl;
    @TableField(exist = false)
    private JobMessage messageDetail;
}
