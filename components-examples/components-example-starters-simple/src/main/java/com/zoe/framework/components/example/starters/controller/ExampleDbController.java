package com.zoe.framework.components.example.starters.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zoe.framework.components.example.starters.entity.msyql.Example1;
import com.zoe.framework.components.example.starters.service.Example1MysqlService;
import com.zoe.framework.components.starter.database.wrapper.PageQuery;
import com.zoe.framework.components.starter.id.service.IdService;
import com.zoe.framework.components.util.value.data.RandomValueUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

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
    @GetMapping("/exampleMysqlQueryPage")
    public IPage<Example1> exampleMysqlQuery(PageQuery pageQuery) {
        return example1MysqlService.page(pageQuery.toPage());
    }

}
