package com.skrstop.framework.components.example.starters.cloud.feign;

import com.skrstop.framework.components.core.common.response.ListResult;
import com.skrstop.framework.components.core.common.response.page.ListSimplePageData;
import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeign;
import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeignController;
import com.skrstop.framework.components.example.starters.cloud.feign.api.RemoteFeignOrigin;
import com.skrstop.framework.components.example.starters.cloud.feign.controller.LocalController;
import com.skrstop.framework.components.example.starters.cloud.feign.controller.RemoteServerController;
import com.skrstop.framework.components.example.starters.cloud.feign.entity.DemoInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CloudFeignUnitTest {

    // ---------------- DemoInfo ----------------

    @Test
    public void testDemoInfo() {
        DemoInfo info = new DemoInfo();
        info.setName("n");
        assertEquals("n", info.getName());
        assertNotNull(info.toString());
        assertEquals(info, info);
        assertNotEquals(info, new DemoInfo());
        assertNotNull(DemoInfo.builder().name("x").build());
        assertNotNull(new DemoInfo("y"));
    }

    // ---------------- ApplicationBootstrap ----------------

    @Test
    public void testMain() {
        try (org.mockito.MockedStatic<org.springframework.boot.SpringApplication> mocked =
                     Mockito.mockStatic(org.springframework.boot.SpringApplication.class)) {
            ExampleStartersCloudFeignApplicationBootstrap.main(new String[0]);
            mocked.verify(() -> org.springframework.boot.SpringApplication
                    .run(ExampleStartersCloudFeignApplicationBootstrap.class, new String[0]));
        }
        assertTrue(true);
    }

    // ---------------- RemoteServerController ----------------

    @Test
    public void testRemoteServerController() {
        RemoteServerController controller = new RemoteServerController();
        try {
            controller.exampleFeign1(new DemoInfo("n"));
            fail();
        } catch (RuntimeException expected) {
            assertEquals("test exception", expected.getMessage());
        }
        assertNull(controller.exampleFeign2(null));
        assertEquals(List.of("a"), controller.exampleFeign2(List.of("a")));
        assertEquals("name", controller.exampleFeign3("id").getName());
        assertEquals(10, controller.exampleFeign4(new HashMap<>()).size());
        ListSimplePageData<DemoInfo> page = controller.exampleFeign5(List.of("a"));
        assertEquals(10, page.getRows().size());
        controller.exampleFeign6();
        assertNull(controller.exampleFeign7());
    }

    // ---------------- LocalController ----------------

    @Test
    public void testLocalController() {
        LocalController controller = new LocalController();
        RemoteFeign remoteFeign = Mockito.mock(RemoteFeign.class);
        RemoteFeignController remoteFeignController = Mockito.mock(RemoteFeignController.class);
        RemoteFeignOrigin remoteFeignOrigin = Mockito.mock(RemoteFeignOrigin.class);
        Mockito.when(remoteFeign.exampleFeign51(Mockito.anyList())).thenReturn(new ListSimplePageData<>());
        Mockito.when(remoteFeignOrigin.exampleFeign4(Mockito.any())).thenReturn(ListResult.Builder.error());
        Mockito.when(remoteFeignController.exampleFeign4(Mockito.any())).thenReturn(List.of(new DemoInfo()));
        ReflectionTestUtils.setField(controller, "remoteFeign", remoteFeign);
        ReflectionTestUtils.setField(controller, "remoteFeignController", remoteFeignController);
        ReflectionTestUtils.setField(controller, "remoteFeignOrigin", remoteFeignOrigin);

        assertEquals("success", controller.exampleRemote1());
        controller.exampleTest();

        Mockito.verify(remoteFeign).exampleFeign51(Mockito.anyList());
        // exampleTest 内为压测循环，调用次数不确定，仅验证至少调用一次
        Mockito.verify(remoteFeignOrigin, Mockito.atLeast(1)).exampleFeign4(Mockito.any());
        Mockito.verify(remoteFeignController, Mockito.atLeast(1)).exampleFeign4(Mockito.any());
    }

    // ---------------- fallbacks ----------------

    @Test
    public void testRemoteFeignFallback() {
        RemoteFeign.RemoteFeignClientFallBack fallback = new RemoteFeign.RemoteFeignClientFallBack();
        assertNotNull(fallback.exampleFeign1(new DemoInfo()));
        assertNotNull(fallback.exampleFeign2(List.of("a")));
        assertNotNull(fallback.exampleFeign3("id"));
        assertNotNull(fallback.exampleFeign4(new HashMap<>()));
        assertNotNull(fallback.exampleFeign5(List.of("a")));
        assertNotNull(fallback.exampleFeign51(List.of("a")));
        assertNotNull(fallback.exampleFeign6());

        RemoteFeign.AccessClientFallBackFactory factory = new RemoteFeign.AccessClientFallBackFactory();
        RemoteFeign created = factory.create(new Throwable("boom"));
        assertNotNull(created);
        assertNotNull(created.exampleFeign6());
    }

    @Test
    public void testRemoteFeignOriginFallback() {
        RemoteFeignOrigin.RemoteFeignClientFallBack fallback = new RemoteFeignOrigin.RemoteFeignClientFallBack();
        assertNotNull(fallback.exampleFeign1(new DemoInfo()));
        assertNotNull(fallback.exampleFeign2(List.of("a")));
        assertNotNull(fallback.exampleFeign3("id"));
        assertNotNull(fallback.exampleFeign4(new HashMap<>()));

        RemoteFeignOrigin.AccessClientFallBackFactory factory = new RemoteFeignOrigin.AccessClientFallBackFactory();
        RemoteFeignOrigin created = factory.create(new Throwable("boom"));
        assertNotNull(created.exampleFeign4(new HashMap<>()));
    }
}
