package com.skrstop.framework.components.example.starters.simple.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.skrstop.framework.components.core.exception.SkrstopBusinessException;
import com.skrstop.framework.components.example.starters.simple.entity.msyql.Example1;
import com.skrstop.framework.components.example.starters.simple.service.Example1MysqlService;
import com.skrstop.framework.components.starter.database.annotation.DSDatabaseTransactional;
import com.skrstop.framework.components.starter.database.constant.DatabaseConst;
import com.skrstop.framework.components.starter.database.wrapper.PageQuery;
import com.skrstop.framework.components.starter.id.service.IdService;
import com.skrstop.framework.components.util.value.data.RandomValueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 蒋时华
 * @date 2023-11-30 17:52:44
 */
@RestController
@Slf4j
@Validated
public class ExampleDbController {

    @Autowired
    private Example1MysqlService example1MysqlService;
    @Autowired
    private IdService idService;

    /**
     * mysql 插入样例
     */
    @GetMapping("/exampleMysqlInsert")
    public void exampleMysqlInsert() {
        example1MysqlService.save(Example1.builder()
                .id(idService.getId())
                .name(RandomValueUtil.getChineseName())
                .age(RandomValueUtil.getNum(1, 100))
                .birth(LocalDateTime.now())
                .die(RandomValueUtil.getNum(1, 100) > 50)
                .status(RandomValueUtil.getNum(1, 100) > 50)
                .build());
    }

    /**
     * mysql 插入样例
     */
    @Transactional(DatabaseConst.TRANSACTION_NAME_DATABASE)
    @GetMapping("/exampleMysqlUpdate")
    public void exampleMysqlUpdate() {
        // 1730515515425820672
        example1MysqlService.update(Wrappers.lambdaUpdate(Example1.class)
                .set(Example1::getName, RandomValueUtil.getChineseName())
                .set(Example1::getAge, RandomValueUtil.getNum(1, 100))
                .set(Example1::getBirth, LocalDateTime.now())
                .set(Example1::getDie, RandomValueUtil.getNum(1, 100) > 50)
                .set(Example1::getStatus, RandomValueUtil.getNum(1, 100) > 50)
                .eq(Example1::getId, 1730515515425820672L)
        );
    }

    /**
     * mysql 查询样例
     *
     * @return
     */
    @GetMapping("/exampleMysqlQuery")
    public List<Example1> exampleMysqlQuery() {
        return example1MysqlService.list();
    }

    /**
     * mysql 分页查询样例
     *
     * @return
     */
    @com.skrstop.framework.components.starter.database.annotation.Transactional
    @GetMapping("/exampleMysqlQueryPage")
    public IPage<Example1> exampleMysqlQuery(PageQuery pageQuery) {
        return example1MysqlService.page(pageQuery.toPage());
    }


    /**
     * 动态数据源样例
     */
    @GetMapping("/exampleDynamic")
    @DSDatabaseTransactional(rollbackFor = Exception.class)
    public Map<String, List<Example1>> exampleDynamic(boolean exception) {
        List<Example1> db0 = example1MysqlService.addDb0();
        if (exception) {
            throw new SkrstopBusinessException("模拟本地事务异常回滚");
        }
        List<Example1> db1 = example1MysqlService.addDb1();
        Map<String, List<Example1>> result = new LinkedHashMap<>();
        result.put("db0", db0);
        result.put("db1", db1);
        return result;
    }

//    @DsDatabaseTxEventListener(phase = TransactionPhase.BEFORE_COMMIT)
//    public void txEvent(Object obj) {
//        System.out.println("aaaa");
//    }

    /**
     * 动态数据源spel
     *
     * @param key
     * @return
     */
    @GetMapping("/exampleDynamic2")
    public List<Example1> exampleDynamic2(String key) {
        return example1MysqlService.listByKey(key);
    }

}
