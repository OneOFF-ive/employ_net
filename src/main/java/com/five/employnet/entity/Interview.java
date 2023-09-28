package com.five.employnet.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Interview implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String interview_id;
    private String user_id;
    private String company_name;
    private String job_name;
    private LocalDateTime interview_time;
    private String info_detail;
}
