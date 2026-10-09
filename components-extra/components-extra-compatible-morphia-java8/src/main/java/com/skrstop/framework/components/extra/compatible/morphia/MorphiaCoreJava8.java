package com.skrstop.framework.components.extra.compatible.morphia;

/**
 * 标记类：标识本构件为 morphia-core {@code 2.5.3} 的 JDK8 重编译版本。
 * <p>
 * 上游官方构件 {@code dev.morphia.morphia:morphia-core:2.5.3} 为 Java 11 字节码，
 * 无法在 Java 8 项目中使用，因此本构件使用 JDK8 重新编译后随构件发布，
 * 类名与上游完全一致（{@code dev.morphia.**}），可直接替代上游构件使用。
 * </p>
 * <p>
 * 源码来源：Morphia {@code 2.5.3_11_8} JDK8 移植分支，commit {@code 713aedae0b4}（2026-10-09），
 * 与 {@code lib/morphia-core-2.5.3.jar} 为同一快照构建；完整源码已包含在本构件的
 * {@code -sources.jar} 中，重新编译方式见模块 {@code README.md}。
 * </p>
 *
 * @author skrstop
 * @see <a href="https://github.com/MorphiaOrg/morphia">MorphiaOrg/morphia</a>
 */
public final class MorphiaCoreJava8 {

    /**
     * 上游 morphia-core 版本号
     */
    public static final String MORPHIA_VERSION = "2.5.3";

    private MorphiaCoreJava8() {
    }
}
