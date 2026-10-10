package com.skrstop.framework.components.example.starters.simple;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.skrstop.framework.components.example.starters.simple.entity.msyql.Example1;
import com.skrstop.framework.components.example.starters.simple.service.Example1MysqlService;
import com.skrstop.framework.components.starter.database.wrapper.PageQuery;
import com.skrstop.framework.components.starter.id.service.IdService;
import com.skrstop.framework.components.util.value.data.RandomValueUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.Socket;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * example-starters-simple 基础功能集成验证。
 * <p>
 * 依赖外部中间件（redis 6379 / mongo 27017）的用例通过端口探测自动跳过，
 * H2(mysql) 与 web 链路无外部依赖，始终执行。
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleStartersSimpleIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private Example1MysqlService example1MysqlService;
    @Autowired
    private IdService idService;

    private static boolean redisAvailable;
    private static boolean mongoAvailable;

    @BeforeAll
    static void probeMiddleware() {
        redisAvailable = probe(6379);
        mongoAvailable = probe(27017);
        log.info("middleware probe: redis={} mongo={}", redisAvailable, mongoAvailable);
    }

    private static boolean probe(int port) {
        try (Socket s = new Socket("127.0.0.1", port)) {
            return s.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    // ---------------- context & web ----------------

    @Test
    void contextLoads() {
        assertNotNull(example1MysqlService);
        assertNotNull(idService);
    }

    @Test
    void exampleValid_ok() {
        ResponseEntity<String> resp = restTemplate.getForEntity("/exampleValid?val=abc", String.class);
        assertEquals(200, resp.getStatusCode().value());
        assertTrue(resp.getBody() != null && !resp.getBody().isEmpty());
    }

    @Test
    void exampleValid_blankShouldError() {
        ResponseEntity<String> resp = restTemplate.getForEntity("/exampleValid", String.class);
        // 参数校验失败由全局异常处理器兜底（4xx 或错误响应体均视为校验生效）
        assertTrue(resp.getStatusCode().value() >= 400
                || (resp.getBody() != null && resp.getBody().contains("val")));
    }

    // ---------------- mysql / mybatis-plus (H2) ----------------

    @Test
    void mysqlInsertAndQuery() {
        long id = idService.getId();
        Example1 record = Example1.builder()
                .id(id)
                .name(RandomValueUtil.getChineseName())
                .age(18)
                .birth(LocalDateTime.now())
                .die(false)
                .status(true)
                .build();
        assertTrue(example1MysqlService.save(record));

        Example1 loaded = example1MysqlService.getById(id);
        assertNotNull(loaded, "insert 后应能按 id 查回");
        assertEquals(18, loaded.getAge());
        assertEquals(record.getName(), loaded.getName());
    }

    @Test
    void mysqlUpdateAndPage() {
        long id = idService.getId();
        Example1 record = Example1.builder()
                .id(id)
                .name(RandomValueUtil.getChineseName())
                .age(20)
                .birth(LocalDateTime.now())
                .die(false)
                .status(false)
                .build();
        example1MysqlService.save(record);

        record.setAge(99);
        assertTrue(example1MysqlService.updateById(record));

        Example1 loaded = example1MysqlService.getById(id);
        assertNotNull(loaded);
        assertEquals(99, loaded.getAge());

        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNumber(1);
        pageQuery.setPageSize(10);
        IPage<Example1> page = example1MysqlService.page(pageQuery.toPage());
        assertNotNull(page);
        assertTrue(page.getTotal() >= 1, "分页应至少包含 1 条记录");
        assertTrue(page.getRecords().size() >= 1);
    }

    // ---------------- redis / redisson（无 redis 时跳过） ----------------

    @Test
    void redisPipeline() {
        Assumptions.assumeTrue(redisAvailable, "本机无 redis，跳过");
        ResponseEntity<String> resp = restTemplate.getForEntity("/exampleRedisPipline", String.class);
        assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void redisson() {
        Assumptions.assumeTrue(redisAvailable, "本机无 redis，跳过");
        ResponseEntity<String> resp = restTemplate.getForEntity("/exampleRedisson", String.class);
        assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void redisDynamic() {
        Assumptions.assumeTrue(redisAvailable, "本机无 redis，跳过");
        ResponseEntity<String> resp = restTemplate.getForEntity("/exampleRedisDynamic", String.class);
        assertEquals(200, resp.getStatusCode().value());
    }

}
