package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data

public class EducationExperience implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private String education_experience_id;
    private String talent_id;
    private String degree;
    private String major;
    private String school;
    private String time;
}
