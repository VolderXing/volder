package com.example.usercentertest.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -7096674461284447274L;
    private long id;
}
