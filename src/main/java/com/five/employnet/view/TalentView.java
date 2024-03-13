package com.five.employnet.view;

import com.baomidou.mybatisplus.annotation.TableName;
import com.five.employnet.entity.Talent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("talent_view")
public class TalentView extends Talent {

    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String phone_number;
    private String location;
    private String sex;
    private String education;
}
