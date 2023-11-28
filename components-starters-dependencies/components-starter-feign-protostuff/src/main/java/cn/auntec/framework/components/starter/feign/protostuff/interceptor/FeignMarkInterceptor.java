package cn.auntec.framework.components.starter.feign.protostuff.interceptor;

import cn.auntec.framework.components.starter.feign.protostuff.annotation.ProtostuffFeignClient;
import cn.auntec.framework.components.util.constant.FeignConst;
import cn.auntec.framework.components.util.value.data.ObjectUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * feign 请求拦截，添加protostuff标记
 *
 * @author 蒋时华
 * @date 2019-04-21
 */
@Slf4j
public class FeignMarkInterceptor implements RequestInterceptor {

    private static final Map<String, ProtostuffFeignClient> PROTOSTUFF_FEIGN_CLIENT_CACHE = new HashMap<>();

    @Override
    public void apply(RequestTemplate template) {

//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        try {
            String name = template.feignTarget().type().getName();
            ProtostuffFeignClient protostuffFeignClient = PROTOSTUFF_FEIGN_CLIENT_CACHE.get(name);
            if (protostuffFeignClient == null) {
                protostuffFeignClient = Class.forName(name).getAnnotation(ProtostuffFeignClient.class);
                PROTOSTUFF_FEIGN_CLIENT_CACHE.put(name, protostuffFeignClient);
            }
            if (ObjectUtil.isNotNull(protostuffFeignClient)) {
                // 添加feignMark 标记为protostuff
                template.header(FeignConst.USE_FEIGN_NAME, FeignConst.USE_FEIGN_VALUE);
                template.header(FeignConst.FEIGN_PROTOCOL_NAME, FeignConst.FEIGN_PROTOCOL_VALUE_PROTOSTUFF);
            }
        } catch (ClassNotFoundException e) {
            // do nothing
        }


    }

}
