package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String user_id;
    private String open_id;
    private String session_key;
    private String sex;
    private String name;
    private String profile_url;
    private String phone_number;
    private LocalDate birthday;
    private String marriage;
    private String education;
    private String experience;
    private String user_class;
}
