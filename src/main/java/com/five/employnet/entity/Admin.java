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
}
