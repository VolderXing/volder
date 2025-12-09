package com.example.usercentertest.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TeamJoinRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6599907075670560987L;
    /**
     * id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;
}
