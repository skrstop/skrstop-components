package com.skrstop.framework.components.starter.annotation.handle.function.privacyInfo;

import java.util.Set;

/**
 * @author 蒋时华
 * @date 2024-05-10 20:59:24
 * @since 1.0.0
 */
public interface PrivacyInfoTypeRule {

    /*** 支持的处理类型 */
    Set<String> supportType();

    /**
     * 脱敏处理
     *
     * @param originVal
     * @return
     */
    String handle(String originVal);

}
