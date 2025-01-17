package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class Talent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String talent_id;
    private String user_id;
    private int age;
    private String eduction_level;
    private String home_location;
    private String name;
    private String phone;
    private String major;
    private String profile_photo;
    private String resume;
    private String resume_file;
    private String resume_level;
    private String school;
    private String self_introduce;
    private String sex;
    private String state;
    private String user_class;

    @TableField(exist = false)
    private List<EductionExperience> eduction_experience;

    @TableField(exist = false)
    private Experience experience;

    @TableField(exist = false)
    private List<JobIntention> job_intention;

    @JsonIgnore
    private String intention_msg;

    private String status;
}
