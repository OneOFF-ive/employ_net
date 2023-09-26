package com.five.employnet.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class Company implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String company_id;
    private String openId;
    private String sessionKey;
    private String nickName;
    private String gender;
    private String avatarUrl;       //头像地址
    private String name;
    private String companyClass;    //公司性质
    private String scale;           //公司规模
    private String business;        //公司行业
    private String address;
    private String work_detail;     //工作细节
    @TableField(exist = false)
    private List<Welfare> welfareList; //福利列表
    private String introduce;
    private String linkPerson;     //联系人
    private String phoneNumber;
    @TableField(exist = false)
    private List<Annex> annexList;   //附件列表
}
