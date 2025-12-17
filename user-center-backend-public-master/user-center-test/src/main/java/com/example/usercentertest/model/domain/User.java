package com.example.usercentertest.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.TreeMap;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    // 添加序列化版本UID
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    @TableField("username")
    private String username;

    /**
     * 账号
     */
    @TableField("userAccount")
    private String userAccount;

    /**
     * 用户头像
     */
    @TableField("avatarUrl")
    private String avatarUrl;

    /**
     * 性别
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 密码
     */
    @TableField("userPassword")
    private String userPassword;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 状态 0 - 正常
     */
    @TableField("userStatus")
    private Integer userStatus;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     *
     */
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("isDelete")
    private Integer isDelete;

    /**
     *
     */
    @TableField("userRole")
    private Integer userRole;

    /**
     *
     */
    @TableField("planetCode")
    private String planetCode;


    /**
     * 标签列表
     */
    @TableField("tags")
    private String tags;
}