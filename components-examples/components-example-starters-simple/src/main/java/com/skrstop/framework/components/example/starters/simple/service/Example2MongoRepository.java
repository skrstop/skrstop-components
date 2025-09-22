package com.skrstop.framework.components.example.starters.simple.service;

import com.skrstop.framework.components.example.starters.simple.entity.mongo.Example2Mongo;
import com.skrstop.framework.components.example.starters.simple.entity.mongo.ExampleMongoChild;
import com.skrstop.framework.components.starter.mongodb.annotation.DSMongo;
import com.skrstop.framework.components.starter.mongodb.service.impl.SuperRepositoryImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author 蒋时华
 * @date 2023-12-01 09:52:25
 */
@Service
public class Example2MongoRepository extends SuperRepositoryImpl<Example2Mongo> {

    @DSMongo
    public void saveMaster(Long id) {
        Example2Mongo example1 = Example2Mongo.builder()
                .id(id)
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
    public void saveSlave(Long id) {
        Example2Mongo example1 = Example2Mongo.builder()
                .id(id)
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
