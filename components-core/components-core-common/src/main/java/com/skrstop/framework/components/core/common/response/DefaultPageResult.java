package com.skrstop.framework.components.core.common.response;

import com.skrstop.framework.components.core.common.response.abstracts.AbstractPageResult;
import com.skrstop.framework.components.core.common.response.common.CommonResultCode;
import com.skrstop.framework.components.core.common.response.core.IPageResult;
import com.skrstop.framework.components.core.common.response.core.IResult;
import com.skrstop.framework.components.core.common.response.page.PageData;
import com.skrstop.framework.components.core.common.response.page.SimplePageData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 蒋时华
 * @date 2020-05-02 16:36:09
 */
@Getter
@Setter
@SuppressWarnings("all")
public class DefaultPageResult extends AbstractPageResult implements IPageResult {

    private static final long serialVersionUID = 9019006933206591705L;

    public DefaultPageResult() {

    }

    public DefaultPageResult(IResult iResult) {
        super(iResult);
    }

    public DefaultPageResult(IPageResult iPageResult) {
        super(iPageResult);
    }

    public DefaultPageResult(IResult iResult, PageData pageData) {
        super(iResult, pageData);
    }

    public DefaultPageResult(IResult iResult, long pageNumber, long pageSize) {
        super(iResult, new SimplePageData(pageNumber, pageSize));
    }

    public DefaultPageResult(IResult iResult, long pageNumber, long pageSize, long total) {
        super(iResult, new SimplePageData(pageNumber, pageSize, total));
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
        public static DefaultPageResult success(long pageNumber, long pageSize) {
            return new DefaultPageResult(CommonResultCode.SUCCESS, pageNumber, pageSize);
        }

        /**
         * 成功返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult success(long pageNumber, long pageSize, long total) {
            return new DefaultPageResult(CommonResultCode.SUCCESS, pageNumber, pageSize, total);
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
        public static DefaultPageResult error(long pageNumber, long pageSize) {
            return new DefaultPageResult(CommonResultCode.FAIL, pageNumber, pageSize);
        }

        /**
         * 默认请求失败返回值，不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult error(long pageNumber, long pageSize, long total) {
            return new DefaultPageResult(CommonResultCode.FAIL, pageNumber, pageSize, total);
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
        public static DefaultPageResult result(IResult iResult) {
            return new DefaultPageResult(iResult);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult iResult, long pageNumber, long pageSize) {
            return new DefaultPageResult(iResult, pageNumber, pageSize);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult iResult, long pageNumber, long pageSize, long total) {
            return new DefaultPageResult(iResult, pageNumber, pageSize, total);
        }

        /**
         * 请求返回值, 不携带数据
         *
         * @return Result
         */
        public static DefaultPageResult result(IResult iResult, PageData pageData) {
            return new DefaultPageResult(iResult, pageData);
        }

    }

}
