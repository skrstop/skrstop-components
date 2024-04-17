package com.skrstop.framework.components.util.event.entity;

import com.skrstop.framework.components.core.common.serializable.SerializableBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2022-01-20 14:03:09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ShutdownTaskEventResult extends SerializableBean {
    private static final long serialVersionUID = 1655869834939632281L;

    /*** 是否已经结束 */
    private boolean shutdown;
    private boolean finish;
    /*** 自定义查询信息 */
    private Serializable customQueryResult;
    /*** 任务处理结果列表 */
    private List<ShutdownTaskEventResultItem> taskResult;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Accessors(chain = true)
    public static class ShutdownTaskEventResultItem extends SerializableBean {
        private static final long serialVersionUID = 1458637651664376300L;

        /*** task名 */
        private String name;
        /*** 当前总共任务 */
        private Integer currentTotalTask;
        /*** 当前正在处理中的任务 */
        private Integer currentHandlingTask;
        /*** 当前已经结束的任务 */
        private Integer currentEndTask;
        /*** 当前正在等待的任务的任务 */
        private Integer currentWaitingTask;
    }

}
