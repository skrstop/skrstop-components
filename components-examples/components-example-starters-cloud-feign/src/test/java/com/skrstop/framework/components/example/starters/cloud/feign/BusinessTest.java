package com.skrstop.framework.components.example.starters.cloud.feign;

import com.skrstop.framework.components.core.common.response.ListResult;
import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeign;
import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeignController;
import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeignOrigin;
import com.skrstop.framework.components.example.starters.cloud.feign.entity.DemoInfo;
import com.skrstop.framework.components.util.stress.StressStoreUtil;
import com.skrstop.framework.components.util.stress.result.StressResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

/**
 * @author 蒋时华
 * @date 2025-01-17 23:04:35
 * @since 1.0.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BusinessTest {

    @Autowired
    private RemoteFeign remoteFeign;
    @Autowired
    private RemoteFeignController remoteFeignController;
    @Autowired
    private RemoteFeignOrigin remoteFeignOrigin;

    @Test
    public void feignPbTest() {
        for (int i = 0; i < 1; i++) {
            testExecute();
        }
    }

    public void testExecute() {
        // 5k
        String longParam = "skrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstopskrstop";
        String finalLongParam = "";
        for (int i = 0; i < 10; i++) {
            finalLongParam += longParam;
        }
        String finalLongParam1 = finalLongParam;
        // 原始
        StressResult originTest = StressStoreUtil.test(4, 2000, () -> {
            ListResult<DemoInfo> result = remoteFeignOrigin.exampleFeign4(new HashMap<String, String>() {{
                put("name", finalLongParam1);
                put("age", "18");
            }});
            if (result.isFailed()) {
                System.out.println("failed");
            }
        }, 10);
        // pb
//        StressResult pbTest = StressStoreUtil.test(4, 2000, () -> {
//            ListResult<DemoInfo> result = remoteFeign.exampleFeign4(new HashMap<String, String>() {{
//                put("name", finalLongParam1);
//                put("age", "18");
//            }});
//            if (result.isFailed()) {
//                System.out.println("failed");
//            }
//        }, 10);
//        // 控制台输出
        System.out.println("原始");
        System.out.println(StressStoreUtil.format(originTest));
        System.out.println("pb");
//        System.out.println(StressStoreUtil.format(pbTest));
    }


}
