package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String user_id;
    private String openId;
    private String sessionKey;
    private String sex;
    private String name;
    private String profileUrl;
    private String phoneNumber;
    private LocalDate birthday;
    private String marriage;
    private String education;
    private String experience;
    private String userClass;
}
