package com.skrstop.framework.components.example.starters.simple.controller;

import com.skrstop.framework.components.core.common.response.page.ListSimplePageData;
import com.skrstop.framework.components.example.starters.simple.entity.mongo.Example1Mongo;
import com.skrstop.framework.components.example.starters.simple.entity.mongo.Example2Mongo;
import com.skrstop.framework.components.example.starters.simple.entity.mongo.ExampleMongoChild;
import com.skrstop.framework.components.example.starters.simple.service.Example1MongoService;
import com.skrstop.framework.components.example.starters.simple.service.Example2MongoService;
import com.skrstop.framework.components.starter.mongodb.configuration.dynamic.DynamicMongoContextHolder;
import com.skrstop.framework.components.starter.mongodb.wrapper.PageQuery;
import com.skrstop.framework.components.util.value.data.CollectionUtil;
import com.skrstop.framework.components.util.value.lambda.LambdaUtil;
import dev.morphia.query.Query;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperators;
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
public class ExampleMongodbController {

    @Autowired
    private Example1MongoService example1MongoService;
    @Autowired
    private Example2MongoService example2MongoService;

    /**
     * mongo 样例1
     *
     * @return
     */
    @GetMapping("/exampleMongodb1")
    public void exampleMongodb1() {
        Example1Mongo example1 = Example1Mongo.builder()
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
        example1MongoService.save(example1);
        Example2Mongo example2 = Example2Mongo.builder()
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
        example2MongoService.save(example2);
    }

    /**
     * mongo 样例2
     *
     * @return
     */
    @GetMapping("/exampleMongodb2")
    public Example2Mongo exampleMongodb2() {
        Long id = 1971122348874665984L;
        Filter idFilter = Filters.eq("id", id);
//        example2MongoService.update(example2MongoService.find(Arrays.asList(idFilter)), CollectionUtil.newArrayList(
//                UpdateOperators.set(LambdaUtil.convertToFieldName(Example2Mongo::getValStr), "cccccc")
//                , UpdateOperators.set(LambdaUtil.convertToFieldName(Example2Mongo::getValInt), 8888)
//        ));
        example2MongoService.updateWithVersion(14L
                , CollectionUtil.newArrayList(
                        Filters.eq(LambdaUtil.convertToFieldName(Example2Mongo::getId), 1734451410227040256L)
                )
                , CollectionUtil.newArrayList(
                        UpdateOperators.set(LambdaUtil.convertToFieldName(Example2Mongo::getValStr), "ddddd")
                        , UpdateOperators.set(LambdaUtil.convertToFieldName(Example2Mongo::getValInt), 9999)
                ));
        return example2MongoService.findById(id);
    }

    /**
     * mongo 样例3
     *
     * @return
     */
    @GetMapping("/exampleMongodb3")
    public Example2Mongo exampleMongodb3() {
        Query<Example2Mongo> query = example2MongoService.find(CollectionUtil.newArrayList(
                Filters.eq(LambdaUtil.convertToFieldName(Example2Mongo::getId), 1734451410227040256L)
        ));
        example2MongoService.update(query, CollectionUtil.newArrayList(
                UpdateOperators.set(LambdaUtil.convertToFieldName(Example2Mongo::getValStr), "ddddd")
                , UpdateOperators.set(LambdaUtil.convertToFieldName(Example2Mongo::getValInt), 9999)
        ));
        return example2MongoService.findById(1734451410227040256L);
    }

    /**
     * mongo 样例4
     *
     * @return
     */
    @GetMapping("/exampleMongodb4")
    public ListSimplePageData<Example2Mongo> exampleMongodb4(PageQuery pageQuery) {
        return example2MongoService.findPage(pageQuery, CollectionUtil.newArrayList());
    }


    /**
     * mongo 样例5
     *
     * @return
     */
    @GetMapping("/exampleMongodb5")
    public Example2Mongo exampleMongodb5() {
        Example2Mongo byId = example2MongoService.findById(1734451410227040256L);
//        byId.setVersion(1000L);
        example2MongoService.updateWithVersion(byId.getVersion()
                , CollectionUtil.newArrayList(Filters.eq(LambdaUtil.convertToFieldName(Example2Mongo::getId), 1734451410227040256L))
                , CollectionUtil.newArrayList(
                        UpdateOperators.set(LambdaUtil.convertToFieldName(Example2Mongo::getValStr), "111ddddd")
                        , UpdateOperators.set(LambdaUtil.convertToFieldName(Example2Mongo::getValInt), 11119999)
                ));
        return example2MongoService.findById(1734451410227040256L);
    }


    /**
     * mongo 多数据源
     *
     * @return
     */
    @GetMapping("/exampleMongodbMulti1")
    public List<Object> exampleMongodbMulti1() {
        example1MongoService.saveMaster("aaaaaaa111111");
        example1MongoService.saveSlave("bbbbbbb111111");
        Example1Mongo res1 = example1MongoService.findById("aaaaaaa111111");
        DynamicMongoContextHolder.push("slave");
        Example1Mongo res2 = example1MongoService.findById("bbbbbbb111111");
        DynamicMongoContextHolder.poll();


        example2MongoService.saveMaster(11111111L);
        example2MongoService.saveSlave(22222222L);
        Example2Mongo res3 = example2MongoService.findById(11111111L);
        DynamicMongoContextHolder.push("slave");
        Example2Mongo res4 = example2MongoService.findById(22222222L);
        DynamicMongoContextHolder.poll();

        return CollectionUtil.newArrayList(res1, res2, res3, res4);
    }

}
