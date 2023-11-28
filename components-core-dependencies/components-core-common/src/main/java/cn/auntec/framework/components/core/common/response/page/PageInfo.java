package cn.auntec.framework.components.core.common.response.page;

/**
 * @author 蒋时华
 * @date 2020-05-02 15:17:51
 */
public interface PageInfo {

    /*** 获取int当前页数 */
    long getPageNumber();

    /*** 设置当前页数 */
    void setPageNumber(long pageNum);

    /*** 获取页面显示数据大小 */
    long getPageSize();

    /*** 设置页面显示数据大小 */
    void setPageSize(long pageSize);

    /*** 获取总数量 */
    long getTotal();

    /*** 设置总数量 */
    void setTotal(long total);

    /*** 上一个分页信息，用户深度分页 */
    default void setLastPageInfo(String lastPageInfo) {

    }

    /*** 上一个分页信息，用户深度分页 */
    default String getLastPageInfo() {
        return null;
    }

}
