package com.five.employnet.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

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
