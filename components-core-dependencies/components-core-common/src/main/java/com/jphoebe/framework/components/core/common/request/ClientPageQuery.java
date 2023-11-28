package com.jphoebe.framework.components.core.common.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2020-06-05 17:40:07
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Accessors(chain = true)
public class ClientPageQuery implements Serializable {

    private static final long serialVersionUID = -4053239205088003342L;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    /**
     * 当前页
     */
    protected Integer pageNumber;

    /**
     * 每页的数量
     */
    protected Integer pageSize;
    /**
     * 排序的字段名
     */
    protected List<String> ascs;
    /**
     * 排序方式
     */
    protected List<String> descs;
    /**
     * 上一个分页记录，用户深度分页
     */
    private String lastPageInfo;

    public Integer getPageNumber() {
        if (this.pageNumber == null || this.pageNumber <= 0) {
            this.pageNumber = DEFAULT_PAGE_NUMBER;
        }
        return this.pageNumber;
    }

    public Integer getPageSize() {
        if (this.pageSize == null || this.pageSize <= 0) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        }
        return this.pageSize;
    }

    public List<String> getAscs() {
        if (this.ascs == null) {
            this.setAscs(new ArrayList<>());
        }
        return this.ascs;
    }

    public List<String> getDescs() {
        if (this.descs == null) {
            this.setDescs(new ArrayList<>());
        }
        return this.descs;
    }

}
