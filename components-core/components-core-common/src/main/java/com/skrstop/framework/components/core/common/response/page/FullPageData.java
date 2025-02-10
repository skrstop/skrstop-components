package com.skrstop.framework.components.core.common.response.page;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 15:12:37
 */
@Getter
@Setter
public class FullPageData<T> extends AbstractPageInfo<T> implements PageData<T> {

    private static final long serialVersionUID = 8898361203910360871L;
    /*** 总页数 */
    private long totalPage;
    /*** 第一页页码 */
    private long firstPage;
    /*** 最后一页页码 */
    private long lastPage;
    /*** 上一页页码 */
    private long prePage;
    /*** 下一页页码 */
    private long nextPage;
    /*** 是否是首页 */
    private boolean canFirstPage;
    /*** 是否是最后一页 */
    private boolean canLastPage;
    /*** 是否有上一页 */
    private boolean hasPreviousPage;
    /*** 是否有下一页 */
    private boolean hasNextPage;


    public FullPageData() {
    }

    public FullPageData(long pageNumber, long pageSize) {
        this.pageNumber = Math.max(pageNumber, 0);
        this.pageSize = Math.max(pageSize, 0);
        this.setParam();
    }

    public FullPageData(long pageNumber, long pageSize, long total) {
        this.pageNumber = Math.max(pageNumber, 0);
        this.pageSize = Math.max(pageSize, 0);
        this.total = Math.max(total, 0);
        this.setParam();
    }

    private void setParam() {
        this.totalPage = (this.total + this.pageSize - 1) / pageSize;
        this.firstPage = 1;
        this.lastPage = totalPage;
        this.prePage = Math.max(this.pageNumber - 1, 0);
        this.nextPage = Math.min((this.pageNumber + 1), totalPage);
    }

}
