package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@TableName("job")
public class Job implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
//    @JsonProperty("job_id")
    private String job_id;
    private String company_id;
    private String title;
    private String salary;
    private int max_salary;
    private int min_salary;
    private LocalDate time;
    private int number;
    private String job_lab;
    private String message_detail;
    // 工作经验要求
    private String experience;
    // 工作学历要求
    private String degree;
    // 工作年龄要求
    private String age;
    // 工作性别要求
    private String gender;
    private String request;
    private String welfare;
    // 省份
    private String province;
    // 城市
    private String city;
    // 市区
    private String urban;
    // 地址
    private String address;
    // 位置(用于地图定位)
    private String location;
    private String remark;
    private String action;
}
