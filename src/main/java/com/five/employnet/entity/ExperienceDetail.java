package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ExperienceDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private String experience_detail_id;
    private String experience_id;
    private String children_time;
    private String job_detail;
    private String position;
    private String company;
}
