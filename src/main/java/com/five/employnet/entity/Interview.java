package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Interview implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String address;
    private String company_id;
    private String interview_info;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime interview_time;
    private String job_id;
    private boolean receive;
    private boolean reject;
    private String talent_id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime send_time;
    private String info;
    private String style;
}
