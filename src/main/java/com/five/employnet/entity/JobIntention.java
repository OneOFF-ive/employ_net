package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class JobIntention implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private String job_intention_id;
    private String talent_id;
    private String job;
    private String location;
    private String salary;
    private String job_class;
}
