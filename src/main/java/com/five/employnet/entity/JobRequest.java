package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.lang.model.element.NestingKind;
import java.io.Serial;
import java.io.Serializable;

@Data
public class JobRequest  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;
    private String job_id;
    private String major;
    private String age;
    private String education;
    private String sex;
    private String experience;
}
