package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
@SuppressWarnings("all")
public class DefaultPageResult extends AbstractPageResult implements IPageResult {

    @Serial
    private static final long serialVersionUID = 9019006933206591705L;

    public DefaultPageResult() {

    }

    public DefaultPageResult(IResult IResult) {
        super(IResult);
    }

    public DefaultPageResult(IPageResult IPageResult) {
        super(IPageResult);
    }

    public DefaultPageResult(IResult IResult, PageData pageData) {
        super(IResult, pageData);
    }

    public DefaultPageResult(IResult IResult, long pageNum, long pageSize) {
        super(IResult, new SimplePageData(pageNum, pageSize));
    }

    public DefaultPageResult(IResult IResult, long pageNum, long pageSize, long total) {
        super(IResult, new SimplePageData(pageNum, pageSize, total));
    }

    public static class Builder {

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult success() {
            return new DefaultPageResult(CommonResultCode.SUCCESS);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult success(long pageNum, long pageSize) {
            return new DefaultPageResult(CommonResultCode.SUCCESS, pageNum, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult success(long pageNum, long pageSize, long total) {
            return new DefaultPageResult(CommonResultCode.SUCCESS, pageNum, pageSize, total);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult success(PageData pageData) {
            return new DefaultPageResult(CommonResultCode.SUCCESS, pageData);
        }


        /**
         * 默认请求失败返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult error() {
            return new DefaultPageResult(CommonResultCode.FAIL);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult error(long pageNum, long pageSize) {
            return new DefaultPageResult(CommonResultCode.FAIL, pageNum, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult error(long pageNum, long pageSize, long total) {
            return new DefaultPageResult(CommonResultCode.FAIL, pageNum, pageSize, total);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult error(PageData pageData) {
            return new DefaultPageResult(CommonResultCode.FAIL, pageData);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult IResult) {
            return new DefaultPageResult(IResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult IResult, long pageNum, long pageSize) {
            return new DefaultPageResult(IResult, pageNum, pageSize);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult IResult, long pageNum, long pageSize, long total) {
            return new DefaultPageResult(IResult, pageNum, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult IResult, PageData pageData) {
            return new DefaultPageResult(IResult, pageData);
        }

    }

}
