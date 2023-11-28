package cn.auntec.framework.components.util.value.reflect;

import cn.auntec.framework.components.core.annotation.source.Snapshot;
import cn.auntec.framework.components.util.constant.StringPoolConst;
import lombok.experimental.UtilityClass;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.serializers.JavaCodeSerializer;
import org.reflections.util.FilterBuilder;

import java.util.Set;

/**
 * @author 蒋时华
 * @date 2019-04-27
 */
@UtilityClass
public class ReflectionUtil extends ReflectionUtils {

    /**
     * 根据父类类型获取所有子类类型
     *
     * @param type
     * @return
     */
    @Snapshot(message = "新版本未测试")
    public static <T> Set<Class<? extends T>> getSubTypesOf(final String basePackage, final Class<T> type) {
        return Reflections.collect(basePackage
                        , new FilterBuilder().includePattern("*.*")
                        , new JavaCodeSerializer())
                .getSubTypesOf(type);
    }

    /**
     * 根据父类类型获取所有子类类型
     *
     * @param type
     * @return
     */
    @Snapshot(message = "新版本未测试")
    public static <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        return Reflections.collect(StringPoolConst.EMPTY
                        , new FilterBuilder().includePattern("*.*")
                        , new JavaCodeSerializer())
                .getSubTypesOf(type);
    }


}
