package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

//应聘信息
@Data
public class Application implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String application_id;
    private String user_id;
    private String company_name;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime update_time;
}
