package cn.auntec.framework.components.core.common.response.abstracts;

import cn.auntec.framework.components.core.common.serializable.SerializableBean;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:14:04
 */
@Setter
@Getter
public abstract class AbstractPageInfo extends SerializableBean {

    /*** 当前页数 */
    protected long pageNumber;
    /*** 当前每页显示数量 */
    protected long pageSize;
    /*** 总数量 */
    protected long total;
    /*** 上一个分页信息，用户深度分页 */
    private String lastPageInfo;

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
