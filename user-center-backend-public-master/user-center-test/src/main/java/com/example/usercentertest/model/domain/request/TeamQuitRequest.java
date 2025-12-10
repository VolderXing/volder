package com.example.usercentertest.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TeamQuitRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -4828462325724214429L;

    private Long teamId;
}
