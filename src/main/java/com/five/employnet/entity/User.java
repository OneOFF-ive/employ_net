package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;
    private String openId;
    private String sessionKey;
    private String nickName;
    private String gender;
    private String avatarUrl;
    private String name;
    @TableField(exist = false)
    private MultipartFile profileUrl;
    private String phoneNumber;
    private LocalDateTime birthday;
    private String marriage;
    private String education;
    private String experience;
}
