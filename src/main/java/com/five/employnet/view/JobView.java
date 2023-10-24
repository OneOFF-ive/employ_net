package com.five.employnet.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.five.employnet.entity.Job;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("job_view")
public class JobView extends Job {
    @Serial
    private static final long serialVersionUID = 1L;

    private String lab;
    private String business;
    private String company;
    private String avatar_url;
    private String state;
}
