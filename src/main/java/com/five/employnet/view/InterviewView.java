package com.five.employnet.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.five.employnet.entity.Interview;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("interview_view")
public class InterviewView extends Interview {
    @Serial
    private static final long serialVersionUID = 1L;

    private String company_name;
    private String company_icon;
    private String job_title;
    private String talent_name;
    private String talent_photo;
    private String talent_phone;
}
