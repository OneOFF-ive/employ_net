package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Admin implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String admin_id;
    private String username;
    private String password;
    private String roles;
    private String introduction;
    private String avatar;
    private String name;
}
