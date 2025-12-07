package com.example.usercentertest.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("team")
public class Team implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("maxNum")
    private Integer maxNum;

    @TableField("expireTime")
    private Date expireTime;

    @TableField("userId")
    private Long userId;

    @TableField("status")
    private Integer status;

    @TableField("password")
    private String password;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateTime")
    private Date updateTime;

    @TableField("isDelete")
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;
}