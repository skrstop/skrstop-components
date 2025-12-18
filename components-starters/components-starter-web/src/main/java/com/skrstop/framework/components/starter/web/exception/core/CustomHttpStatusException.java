package com.skrstop.framework.components.starter.web.exception.core;

import org.springframework.http.HttpStatus;

/**
 *
 * @author 蒋时华
 * @date 2020-05-16 15:53:06
 */
public interface CustomHttpStatusException {

    HttpStatus getHttpStatus();

}
