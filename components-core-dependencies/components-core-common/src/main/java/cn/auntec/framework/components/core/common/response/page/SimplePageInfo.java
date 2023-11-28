package cn.auntec.framework.components.core.common.response.page;

import cn.auntec.framework.components.core.common.response.abstracts.AbstractPageInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:13:29
 */
@Getter
@Setter
public class SimplePageInfo extends AbstractPageInfo implements PageInfo {

    private static final long serialVersionUID = -7653109079434897802L;

    public SimplePageInfo() {
    }

    public SimplePageInfo(long pageNum, long pageSize) {
        super(pageNum, pageSize);
    }

    public SimplePageInfo(long pageNum, long pageSize, long total) {
        super(pageNum, pageSize, total);
    }
}
