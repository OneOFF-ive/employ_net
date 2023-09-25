package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class JobMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private Long jobId;
    private String detail;
    private String address;
    private String welfare;
    private String duty;
    private String requirement;
    private String salary;
}
