package com.skrstop.framework.components.core.common;

import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import org.junit.Assert;
import org.junit.Test;

/**
 * 覆盖接口 default 方法
 *
 * @author 蒋时华
 */
public class InterfaceDefaultsTest {

    private static class PlainResult implements IResult {
        private Object code;
        private String message;

        @Override
        public Object getCode() {
            return code;
        }

        @Override
        public void setCode(Object code) {
            this.code = code;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public void setMessage(String message) {
            this.message = message;
        }
    }

    private static class PlainPageData implements PageData<String> {
        private long pageNumber;
        private long pageSize;
        private long total;
        private String rows;

        @Override
        public long getPageNumber() {
            return pageNumber;
        }

        @Override
        public void setPageNumber(long pageNumber) {
            this.pageNumber = pageNumber;
        }

        @Override
        public long getPageSize() {
            return pageSize;
        }

        @Override
        public void setPageSize(long pageSize) {
            this.pageSize = pageSize;
        }

        @Override
        public long getTotal() {
            return total;
        }

        @Override
        public void setTotal(long total) {
            this.total = total;
        }

        @Override
        public String getRows() {
            return rows;
        }

        @Override
        public void setRows(String rows) {
            this.rows = rows;
        }
    }

    @Test
    public void testIResultDefaults() {
        PlainResult result = new PlainResult();

        Long ts = result.getTimestamp();
        Assert.assertNotNull(ts);
        result.setTimestamp(1L);

        Assert.assertFalse(result.isSuccess());
        Assert.assertTrue(result.isFailed());

        result.setCode(CommonResultCode.SUCCESS.getCode());
        Assert.assertTrue(result.isSuccess());
        Assert.assertFalse(result.isFailed());
    }

    @Test
    public void testPageDataDefaults() {
        PlainPageData pageData = new PlainPageData();
        Assert.assertNull(pageData.getLastPageInfo());
        pageData.setLastPageInfo("last");
    }

}
