package com.cemetery.domain.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 分页查询DTO基类
 */
@Data
public class PageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码（从1开始）
     */
    private Integer page = 1;

    /**
     * 每页显示条数
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String orderBy;

    /**
     * 排序方式（asc-升序，desc-降序）
     */
    private String sortOrder = "desc";
}
