package com.skrstop.framework.components.example.starters.simple.service;

import com.skrstop.framework.components.example.starters.simple.entity.mongo.Example1Mongo;
import com.skrstop.framework.components.example.starters.simple.entity.mongo.ExampleMongoChild;
import com.skrstop.framework.components.starter.mongodb.annotation.DSMongo;
import com.skrstop.framework.components.starter.mongodb.repository.impl.SuperRepositoryImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2023-12-01 09:52:25
 */
@Service
public class Example1MongoService extends SuperRepositoryImpl<Example1Mongo, String> {

    @DSMongo
    public void saveMaster(String id) {
        Example1Mongo example1 = Example1Mongo.builder()
                .id(id.toString())
                .valStr("aaaaaa")
                .valBol(false)
                .valData(LocalDateTime.now())
                .valLong(11111111L)
                .valInt(11111)
                .child(ExampleMongoChild.builder()
                        .valStr("bbbbbb")
                        .valBol(true)
                        .valData(LocalDateTime.now())
                        .valLong(22222222L)
                        .valInt(22222)
                        .build())
                .build();
        this.save(example1);
    }

    @DSMongo("slave")
    public void saveSlave(String id) {
        Example1Mongo example1 = Example1Mongo.builder()
                .id(id.toString())
                .valStr("aaaaaa")
                .valBol(false)
                .valData(LocalDateTime.now())
                .valLong(11111111L)
                .valInt(11111)
                .child(ExampleMongoChild.builder()
                        .valStr("bbbbbb")
                        .valBol(true)
                        .valData(LocalDateTime.now())
                        .valLong(22222222L)
                        .valInt(22222)
                        .build())
                .build();
        this.save(example1);
    }

}
