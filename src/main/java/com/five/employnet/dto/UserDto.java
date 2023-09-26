package com.five.employnet.dto;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;

public class UserDto {
    @TableId
    private String id;
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
