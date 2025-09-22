package com.skrstop.framework.components.example.starters.simple.service;

import com.skrstop.framework.components.example.starters.simple.entity.msyql.Example1;
import com.skrstop.framework.components.example.starters.simple.mapper.mysql.Example1Mapper;
import com.skrstop.framework.components.starter.database.annotation.DSDatabase;
import com.skrstop.framework.components.starter.database.service.impl.SuperRepositoryImpl;
import com.skrstop.framework.components.starter.id.service.IdService;
import com.skrstop.framework.components.util.value.data.RandomValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 蒋时华
 * @date 2023-12-01 09:52:25
 */
@Service
public class Example1MysqlRepository extends SuperRepositoryImpl<Example1Mapper, Example1> {

    @Autowired
    private IdService idService;

    @DSDatabase("db0")
    public List<Example1> addDb0() {
        super.save(Example1.builder()
                .id(idService.getId())
                .name(RandomValueUtil.getChineseName())
                .age(RandomValueUtil.getNum(1, 100))
                .birth(LocalDateTime.now())
                .die(RandomValueUtil.getNum(1, 100) > 50)
                .status(RandomValueUtil.getNum(1, 100) > 50)
                .build());
        return super.list();
    }

    @DSDatabase("db1")
    public List<Example1> addDb1() {
        super.save(Example1.builder()
                .id(idService.getId())
                .name(RandomValueUtil.getChineseName())
                .age(RandomValueUtil.getNum(1, 100))
                .birth(LocalDateTime.now())
                .die(RandomValueUtil.getNum(1, 100) > 50)
                .status(RandomValueUtil.getNum(1, 100) > 50)
                .build());
        return super.list();
    }

    @DSDatabase("#dbKey")
    public List<Example1> listByKey(String dbKey) {
        return super.list();
    }


}
