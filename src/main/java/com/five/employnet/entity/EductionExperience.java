package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data

public class EductionExperience implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private String eduction_experience_id;
    private String talent_id;
    private String digree;
    private String major;
    private String school;
    private String time;
}
