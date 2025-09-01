package com.zwy.aicodegenerator.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页显示条数
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方式
     */
    private String orderType = "desc";

    /**
     * 主键
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
