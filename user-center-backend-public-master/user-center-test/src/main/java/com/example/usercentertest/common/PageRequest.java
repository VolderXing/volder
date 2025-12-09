package com.example.usercentertest.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 8212002675257213457L;
    /**
     * 当前是第几页
     */
    private int pageNum = 1;
    /**
     * 页面大小
     */
    private int pageSize = 10;
}
