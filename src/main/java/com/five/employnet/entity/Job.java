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
//    @JsonProperty("job_id")
    private String job_id;
    private String company_id;
    private String title;
    private String salary;
    private LocalDate time;
    private int number;
    private String lab;
    private String job_lab;
    private String business;
    private String company;
    private String avatar_url;
    @TableField(exist = false)
    private JobMessage message_detail;
}
