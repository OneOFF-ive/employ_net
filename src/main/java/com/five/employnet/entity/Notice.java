package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class Notice implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private String lid;
    private LocalDate date;
    @TableField(exist = false)
    private List<NoticeMessage> message_list;
}
