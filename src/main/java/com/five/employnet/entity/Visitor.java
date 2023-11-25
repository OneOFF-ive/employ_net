package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.five.employnet.service.VisitorService;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Visitor implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    private String ip;
    private String user_id;
    private LocalDateTime time;
    private LocalDate date;

    public Visitor(String user_id, String ip, LocalDateTime time, LocalDate date) {
        this.ip = ip;
        this.user_id = user_id;
        this.time = time;
        this.date = date;
    }
}
