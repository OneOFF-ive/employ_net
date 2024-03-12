package com.five.employnet.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private String user_id;
    private String sex;
    private String name;
    private String profile_url;
    private String phone_number;
    private LocalDate birthday;
    private String marriage;
    private String education;
    private String user_class;
    private String location;

}
