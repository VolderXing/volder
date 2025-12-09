package com.xc.partner.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 860838171065374305L;

    private String useraccount;
    private String userpassword;
    private String checkpassword;
    private String planetcode;
}
