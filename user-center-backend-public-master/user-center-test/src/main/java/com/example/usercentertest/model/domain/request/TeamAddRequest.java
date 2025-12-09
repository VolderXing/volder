package com.example.usercentertest.model.domain.request;

import lombok.Data;
import java.util.Date;

@Data
public class TeamAddRequest {
    private String name;

    private String description;

    private Integer maxNum;

    private Date expireTime;

    private Long userId;

    private Integer status;

    private String password;
}
