package com.skrstop.framework.components.core.common.response.abstracts;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:14:04
 */
@Setter
@Getter
public abstract class AbstractPageInfo<T> implements Serializable {

    /*** 当前页数 */
    protected long pageNumber;
    /*** 当前每页显示数量 */
    protected long pageSize;
    /*** 总数量 */
    protected long total;
    /*** 上一个分页信息，用户深度分页 */
    protected String lastPageInfo;
    protected T rows;

    public AbstractPageInfo() {
    }

    public AbstractPageInfo(long pageNumber, long pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public AbstractPageInfo(long pageNumber, long pageSize, long total) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.total = total;
    }
}
