package com.example.usercentertest.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private String username;

    /**
     * 账号
     */
    private String useraccount;

    /**
     * 用户头像
     */
    private String avatarurl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userpassword;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0 - 正常
     */
    private Integer userstatus;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     *
     */
    private Date updatetime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isdelete;

    /**
     *
     */
    private Integer userrole;

    /**
     *
     */
    private String planetcode;


    /**
     * 标签列表
     */
    private String tags;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
                && (this.getUseraccount() == null ? other.getUseraccount() == null : this.getUseraccount().equals(other.getUseraccount()))
                && (this.getAvatarurl() == null ? other.getAvatarurl() == null : this.getAvatarurl().equals(other.getAvatarurl()))
                && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
                && (this.getUserpassword() == null ? other.getUserpassword() == null : this.getUserpassword().equals(other.getUserpassword()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getUserstatus() == null ? other.getUserstatus() == null : this.getUserstatus().equals(other.getUserstatus()))
                && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()))
                && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()))
                && (this.getIsdelete() == null ? other.getIsdelete() == null : this.getIsdelete().equals(other.getIsdelete()))
                && (this.getUserrole() == null ? other.getUserrole() == null : this.getUserrole().equals(other.getUserrole()))
                && (this.getPlanetcode() == null ? other.getPlanetcode() == null : this.getPlanetcode().equals(other.getPlanetcode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getUseraccount() == null) ? 0 : getUseraccount().hashCode());
        result = prime * result + ((getAvatarurl() == null) ? 0 : getAvatarurl().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getUserpassword() == null) ? 0 : getUserpassword().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getUserstatus() == null) ? 0 : getUserstatus().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        result = prime * result + ((getIsdelete() == null) ? 0 : getIsdelete().hashCode());
        result = prime * result + ((getUserrole() == null) ? 0 : getUserrole().hashCode());
        result = prime * result + ((getPlanetcode() == null) ? 0 : getPlanetcode().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", useraccount=").append(useraccount);
        sb.append(", avatarurl=").append(avatarurl);
        sb.append(", gender=").append(gender);
        sb.append(", userpassword=").append(userpassword);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", userstatus=").append(userstatus);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", isdelete=").append(isdelete);
        sb.append(", userrole=").append(userrole);
        sb.append(", planetcode=").append(planetcode);
        sb.append("]");
        return sb.toString();
    }
}