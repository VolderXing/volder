package com.xc.partner.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3226907435875210660L;
    private String useraccount;
    private String userpassword;
}
