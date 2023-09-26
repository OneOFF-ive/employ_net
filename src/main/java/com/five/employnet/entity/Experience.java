package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class Experience implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private String experience_id;
    private String talent_id;
    private String time;
    @TableField(exist = false)
    private List<ExperienceDetail> list;
}
