package com.example.usercentertest.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户队伍关系
 * @TableName user_team
 */
@Data
@TableName(value = "user_team")
public class UserTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("userId")
    private Long userId;

    /**
     * 队伍id
     */
    @TableField("teamId")
    private Long teamId;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("joinTime")
    private Date joinTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "createTime", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    @TableLogic(value = "0", delval = "1")
    private Integer isDelete;
}