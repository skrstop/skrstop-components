# 三方包版本验收对照表

生成时间：2026-10-10  
数据源：repo1.maven.org 实时元数据（196 坐标穷举审计 + Spring 生态线）

图例：✅ 已最新 ｜ ⬆️ 本轮升级 ｜ 🔒 受限保持（理由见备注）

## 一、Spring 生态（用户指定升级）

| 包                                                  | 升级前     | 当前版本   | 备注                        |
|-----------------------------------------------------|------------|------------|-----------------------------|
| org.springframework.boot:spring-boot-dependencies   | 3.3.13     | 3.5.16     | ✅ 已升级（用户指定）       |
| org.springframework.cloud:spring-cloud-dependencies | 2023.0.6   | 2025.0.3   | ✅ 已升级（用户指定）       |
| com.alibaba.cloud:spring-cloud-alibaba-dependencies | 2023.0.3.4 | 2025.0.0.0 | ✅ 已升级（用户指定）       |
| org.springdoc:springdoc-openapi-starter-webmvc-ui   | 2.6.0      | 2.8.13     | ✅ 2.x 终线（3.x 属 Boot4） |
| io.github.openfeign:feign-core                      | 13.2.1     | 13.6.1     | ✅ Cloud BOM 仲裁线最新     |
| io.seata:seata-spring-boot-starter                  | 2.0.0      | 2.5.0      | ✅ SCA 2025.0.0.0 对齐线    |

## 二、第三方包全量对照（196 坐标）

| 包                                                                 | 当前版本               | repo1 最新         | 状态 | 备注                                                                             |
|--------------------------------------------------------------------|------------------------|--------------------|------|----------------------------------------------------------------------------------|
| cglib:cglib                                                        | 3.3.0                  | 3.3.0              | ✅   |                                                                                  |
| cn.hutool:hutool-bom                                               | 5.8.48                 | 5.8.48             | ✅   |                                                                                  |
| cn.idev.excel:fastexcel                                            | 1.3.0                  | 1.3.0              | ✅   |                                                                                  |
| com.101tec:zkclient                                                | 0.11                   | 0.11               | ✅   |                                                                                  |
| com.alibaba:druid                                                  | 1.2.28                 | 1.2.28             | ✅   |                                                                                  |
| com.alibaba:druid-spring-boot-3-starter                            | 1.2.28                 | 1.2.28             | ✅   |                                                                                  |
| com.alibaba:fastjson                                               | 2.0.65                 | 2.0.65             | ✅   |                                                                                  |
| com.alibaba:simpleimage                                            | 1.2.3                  | 1.2.3              | ✅   |                                                                                  |
| com.alibaba.fastjson2:fastjson2-extension                          | 2.0.65                 | 2.0.65             | ✅   |                                                                                  |
| com.alibaba.fastjson2:fastjson2-extension-spring5                  | 2.0.65                 | 2.0.65             | ✅   |                                                                                  |
| com.alibaba.testable:testable-all                                  | 0.7.9                  | 0.7.9              | ✅   |                                                                                  |
| com.aliyun.oss:aliyun-sdk-oss                                      | 3.18.5                 | 3.18.5             | ✅   |                                                                                  |
| com.atomikos:transactions-jdbc                                     | 6.0.1                  | 6.0.1              | ✅   |                                                                                  |
| com.auth0:java-jwt                                                 | 4.6.1                  | 4.6.1              | ✅   |                                                                                  |
| com.auth0:jwks-rsa                                                 | 0.24.1                 | 0.24.1             | ✅   |                                                                                  |
| com.baomidou:mybatis-plus-spring-boot3-starter                     | 3.5.17                 | 3.5.17             | ✅   |                                                                                  |
| com.baomidou:mybatis-plus-jsqlparser                               | 3.5.17                 | 3.5.17             | ✅   |                                                                                  |
| com.baomidou:mybatis-plus                                          | 3.5.17                 | 3.5.17             | ✅   |                                                                                  |
| com.baomidou:mybatis-plus-generator                                | 3.5.17                 | 3.5.17             | ✅   |                                                                                  |
| com.baomidou:dynamic-datasource-spring                             | 4.5.0                  | 4.5.0              | ✅   |                                                                                  |
| com.belerweb:pinyin4j                                              | 2.5.1                  | 2.5.1              | ✅   |                                                                                  |
| com.caucho:hessian                                                 | 4.0.66                 | 4.0.66             | ✅   |                                                                                  |
| com.esotericsoftware:kryo                                          | 5.7.1                  | 5.7.1              | ✅   |                                                                                  |
| com.gitee.starblues:spring-brick                                   | 3.1.2                  | 3.1.2              | ✅   |                                                                                  |
| com.github.atool:fluent-mybatis                                    | 1.10.1                 | 1.10.1             | ✅   |                                                                                  |
| com.github.atool:fluent-mybatis-processor                          | 1.10.1                 | 1.10.1             | ✅   |                                                                                  |
| com.github.atool:fluent-mybatis-boot-starter                       | 1.10.1                 | 1.10.1             | ✅   |                                                                                  |
| com.github.ben-manes.caffeine:caffeine                             | 3.3.0                  | 3.3.0              | ✅   |                                                                                  |
| com.github.houbb:pinyin                                            | 0.4.0                  | 0.4.0              | ✅   |                                                                                  |
| com.github.houbb:pinyin2hanzi                                      | 0.0.6                  | 0.0.6              | ✅   |                                                                                  |
| com.github.oshi:oshi-core                                          | 7.7.0                  | 7.7.0              | ✅   |                                                                                  |
| com.github.oshi:oshi-core-java11                                   | 7.0.0                  | 7.0.0              | ✅   |                                                                                  |
| com.github.pagehelper:pagehelper                                   | 6.1.1                  | 6.1.1              | ✅   |                                                                                  |
| com.github.stuxuhai:jpinyin                                        | 1.1.8                  | 1.1.8              | ✅   |                                                                                  |
| com.github.yulichang:mybatis-plus-join-boot-starter                | 1.5.9                  | 1.5.9              | ✅   |                                                                                  |
| com.google.code.findbugs:jsr305                                    | 3.0.2                  | 3.0.2              | ✅   |                                                                                  |
| com.google.code.gson:gson                                          | 2.14.0                 | 2.14.0             | ✅   |                                                                                  |
| com.google.errorprone:error_prone_annotations                      | 2.50.0                 | 2.50.0             | ✅   |                                                                                  |
| com.google.zxing:core                                              | 3.5.4                  | 3.5.4              | ✅   |                                                                                  |
| com.googlecode.concurrentlinkedhashmap:concurrentlinkedhashmap-lru | 1.4.2                  | 1.4.2              | ✅   |                                                                                  |
| com.h2database:h2                                                  | 2.5.252                | 2.5.252            | ✅   |                                                                                  |
| com.huaweicloud:esdk-obs-java                                      | 3.26.6                 | 3.26.6             | ✅   |                                                                                  |
| com.lmax:disruptor                                                 | 4.0.0                  | 4.0.0              | ✅   |                                                                                  |
| com.qcloud:cos_api                                                 | 5.6.281                | 5.6.281            | ✅   |                                                                                  |
| com.qcloud:cos-sts_api                                             | 3.1.1                  | 3.1.1              | ✅   |                                                                                  |
| com.squareup.okhttp3:okhttp-jvm                                    | 5.5.0                  | 5.5.0              | ✅   |                                                                                  |
| com.squareup.retrofit2:retrofit                                    | 3.0.0                  | 3.0.0              | ✅   |                                                                                  |
| com.sun.activation:javax.activation                                | 1.2.0                  | 1.2.0              | ✅   |                                                                                  |
| com.sun.jersey:jersey-client                                       | 1.19.4                 | 1.19.4             | ✅   |                                                                                  |
| com.sun.jersey.contribs:jersey-apache-client4                      | 1.19.4                 | 1.19.4             | ✅   |                                                                                  |
| com.twelvemonkeys.bom:bom                                          | 3.15.3                 | 3.15.3             | ✅   |                                                                                  |
| com.vdurmont:emoji-java                                            | 5.1.1                  | 5.1.1              | ✅   |                                                                                  |
| com.xuxueli:xxl-job-core                                           | 3.5.0                  | 3.5.0              | ✅   |                                                                                  |
| com.yomahub:liteflow-spring-boot-starter                           | 2.16.3.1               | 2.16.3.1           | ✅   |                                                                                  |
| com.zaxxer:HikariCP-java7                                          | 2.4.13                 | 2.4.13             | ✅   |                                                                                  |
| com.zaxxer:HikariCP-java9ea                                        | 2.6.1                  | 2.6.1              | ✅   |                                                                                  |
| commons-beanutils:commons-beanutils                                | 1.11.0                 | 1.11.0             | ✅   |                                                                                  |
| commons-cli:commons-cli                                            | 1.11.0                 | 1.11.0             | ✅   |                                                                                  |
| commons-codec:commons-codec                                        | 1.22.1                 | 1.22.1             | ✅   |                                                                                  |
| commons-fileupload:commons-fileupload                              | 1.6.0                  | 1.6.0              | ✅   |                                                                                  |
| commons-io:commons-io                                              | 2.22.0                 | 2.22.0             | ✅   |                                                                                  |
| commons-lang:commons-lang                                          | 2.6                    | 2.6                | ✅   |                                                                                  |
| commons-net:commons-net                                            | 3.13.0                 | 3.13.0             | ✅   |                                                                                  |
| dev.morphia.morphia:morphia-core                                   | 2.5.3                  | 2.5.3              | ✅   |                                                                                  |
| io.github.biezhi:TinyPinyin                                        | 2.0.3.RELEASE          | 2.0.3              | ✅   | 同左，2.0.3.RELEASE 已最新                                                       |
| io.github.mweirauch:micrometer-jvm-extras                          | 0.3.0                  | 0.3.0              | ✅   |                                                                                  |
| io.jsonwebtoken:jjwt                                               | 0.13.0                 | 0.13.0             | ✅   |                                                                                  |
| io.jsonwebtoken:jjwt-impl                                          | 0.13.0                 | 0.13.0             | ✅   |                                                                                  |
| io.jsonwebtoken:jjwt-api                                           | 0.13.0                 | 0.13.0             | ✅   |                                                                                  |
| io.jsonwebtoken:jjwt-jackson                                       | 0.13.0                 | 0.13.0             | ✅   |                                                                                  |
| io.jsonwebtoken:jjwt-orgjson                                       | 0.13.0                 | 0.13.0             | ✅   |                                                                                  |
| io.jsonwebtoken:jjwt-gson                                          | 0.13.0                 | 0.13.0             | ✅   |                                                                                  |
| io.jsonwebtoken:jjwt-root                                          | 0.13.0                 | 0.13.0             | ✅   |                                                                                  |
| io.jsonwebtoken:jjwt-extensions                                    | 0.13.0                 | 0.13.0             | ✅   |                                                                                  |
| io.lettuce:lettuce-core                                            | 6.6.0.RELEASE          | 6.4.0.M1           | ✅   | 排序伪影（.RELEASE 后缀），6.6.0 已最新                                          |
| io.prometheus:simpleclient_common                                  | 0.16.0                 | 0.16.0             | ✅   |                                                                                  |
| io.prometheus:simpleclient                                         | 0.16.0                 | 0.16.0             | ✅   |                                                                                  |
| io.protostuff:protostuff-bom                                       | 1.8.0                  | 1.8.0              | ✅   |                                                                                  |
| io.swagger.core.v3:swagger-core-jakarta                            | 2.2.55                 | 2.2.55             | ✅   |                                                                                  |
| io.swagger.core.v3:swagger-models-jakarta                          | 2.2.55                 | 2.2.55             | ✅   |                                                                                  |
| io.swagger.core.v3:swagger-annotations-jakarta                     | 2.2.55                 | 2.2.55             | ✅   |                                                                                  |
| io.vertx:vertx-core                                                | 5.2.1                  | 5.2.1              | ✅   |                                                                                  |
| io.vertx:vertx-unit                                                | 5.2.1                  | 5.2.1              | ✅   |                                                                                  |
| io.vertx:vertx-web                                                 | 5.2.1                  | 5.2.1              | ✅   |                                                                                  |
| io.vertx:vertx-codegen                                             | 5.2.1                  | 5.2.1              | ✅   |                                                                                  |
| javax.annotation:javax.annotation-api                              | 1.3.2                  | 1.3.2              | ✅   |                                                                                  |
| javax.media.jai:com.springsource.javax.media.jai.core              | 1.1.3                  | —                  | ✅   | repo1 无坐标（冻结终版）                                                         |
| javax.media.jai:com.springsource.javax.media.jai.codec             | 1.1.3                  | —                  | ✅   | repo1 无坐标（冻结终版）                                                         |
| javax.transaction:javax.transaction-api                            | 1.3                    | 1.3                | ✅   |                                                                                  |
| mysql:mysql-connector-java                                         | 8.0.33                 | 8.0.33             | ✅   |                                                                                  |
| net.coobird:thumbnailator                                          | 0.4.21                 | 0.4.21             | ✅   |                                                                                  |
| net.dreamlu:mica-http                                              | 4.1.1                  | 4.1.1              | ✅   |                                                                                  |
| net.sf.dozer:dozer                                                 | 5.5.1                  | 5.5.1              | ✅   |                                                                                  |
| org.apache.commons:commons-dbcp2                                   | 2.14.0                 | 2.14.0             | ✅   |                                                                                  |
| org.apache.commons:commons-lang3                                   | 3.21.0                 | 3.21.0             | ✅   |                                                                                  |
| org.apache.commons:commons-compress                                | 1.28.0                 | 1.28.0             | ✅   |                                                                                  |
| org.apache.commons:commons-pool2                                   | 2.13.1                 | 2.13.1             | ✅   |                                                                                  |
| org.apache.commons:commons-configuration2                          | 2.15.1                 | 2.15.1             | ✅   |                                                                                  |
| org.apache.curator:curator-recipes                                 | 5.9.0                  | 5.9.0              | ✅   |                                                                                  |
| org.apache.dubbo:dubbo                                             | 3.3.6                  | 3.3.6              | ✅   |                                                                                  |
| org.apache.httpcomponents:httpclient                               | 4.5.14                 | 4.5.14             | ✅   |                                                                                  |
| org.apache.httpcomponents.client5:httpclient5                      | 5.6.4                  | 5.6.4              | ✅   |                                                                                  |
| org.apache.logging.log4j:log4j-slf4j-impl                          | 2.26.1                 | 2.26.1             | ✅   |                                                                                  |
| org.apache.logging.log4j:log4j-core                                | 2.26.1                 | 2.26.1             | ✅   |                                                                                  |
| org.apache.logging.log4j:log4j-api                                 | 2.26.1                 | 2.26.1             | ✅   |                                                                                  |
| org.apache.logging.log4j:log4j-jul                                 | 2.26.1                 | 2.26.1             | ✅   |                                                                                  |
| org.apache.logging.log4j:log4j-to-slf4j                            | 2.26.1                 | 2.26.1             | ✅   |                                                                                  |
| org.apache.pdfbox:pdfbox                                           | 3.0.8                  | 3.0.8              | ✅   |                                                                                  |
| org.apache.poi:poi                                                 | 5.5.1                  | 5.5.1              | ✅   |                                                                                  |
| org.apache.poi:poi-ooxml                                           | 5.5.1                  | 5.5.1              | ✅   |                                                                                  |
| org.apache.poi:poi-scratchpad                                      | 5.5.1                  | 5.5.1              | ✅   |                                                                                  |
| org.apache.poi:poi-excelant                                        | 5.5.1                  | 5.5.1              | ✅   |                                                                                  |
| org.apache.poi:poi-ooxml-full                                      | 5.5.1                  | 5.5.1              | ✅   |                                                                                  |
| org.apache.skywalking:apm-toolkit-logback-1.x                      | 9.8.0                  | 9.8.0              | ✅   |                                                                                  |
| org.apache.skywalking:apm-toolkit-log4j-2.x                        | 9.8.0                  | 9.8.0              | ✅   |                                                                                  |
| org.apache.skywalking:apm-toolkit-log4j-1.x                        | 9.8.0                  | 9.8.0              | ✅   |                                                                                  |
| org.apache.skywalking:apm-toolkit-trace                            | 9.8.0                  | 9.8.0              | ✅   |                                                                                  |
| org.apache.skywalking:apm-toolkit-opentracing                      | 9.8.0                  | 9.8.0              | ✅   |                                                                                  |
| org.asynchttpclient:async-http-client-bom                          | 2.16.1                 | 2.16.1             | ✅   |                                                                                  |
| org.bouncycastle:bcprov-jdk18on                                    | 1.86                   | 1.86               | ✅   |                                                                                  |
| org.bouncycastle:bcprov-jdk15on                                    | 1.70                   | 1.70               | ✅   |                                                                                  |
| org.bouncycastle:bcprov-jdk16                                      | 1.46                   | 1.46               | ✅   |                                                                                  |
| org.bouncycastle:bcprov-jdk15                                      | 1.46                   | 1.46               | ✅   |                                                                                  |
| org.bouncycastle:bcprov-jdk15to18                                  | 1.86                   | 1.86               | ✅   |                                                                                  |
| org.bytedeco:javacv-platform                                       | 1.5.14                 | 1.5.14             | ✅   |                                                                                  |
| org.bytedeco:javacv                                                | 1.5.14                 | 1.5.14             | ✅   |                                                                                  |
| org.bytedeco:javacpp-platform                                      | 1.5.14                 | 1.5.14             | ✅   |                                                                                  |
| org.bytedeco:systems-platform                                      | 1.5.14                 | 1.5.14             | ✅   |                                                                                  |
| org.bytedeco:spinnaker-platform                                    | 4.0.0.116-1.5.11       | 4.0.0.116-1.5.11   | ✅   |                                                                                  |
| org.bytedeco:tvm-platform                                          | 0.18.0-1.5.11          | 0.18.0-1.5.11      | ✅   |                                                                                  |
| org.bytedeco:tvm-platform-gpu                                      | 0.18.0-1.5.11          | 0.18.0-1.5.11      | ✅   |                                                                                  |
| org.checkerframework:checker-qual                                  | 4.3.1                  | 4.3.1              | ✅   |                                                                                  |
| org.docx4j:docx4j-JAXB-ReferenceImpl                               | 17.3.2                 | 17.3.2             | ✅   |                                                                                  |
| org.docx4j:docx4j-JAXB-MOXy                                        | 17.3.2                 | 17.3.2             | ✅   |                                                                                  |
| org.docx4j:docx4j-export-fo                                        | 17.3.2                 | 17.3.2             | ✅   |                                                                                  |
| org.glassfish.web:javax.el                                         | 2.2.6                  | 2.2.6              | ✅   |                                                                                  |
| org.javadelight:delight-nashorn-sandbox                            | 0.5.6                  | 0.5.6              | ✅   |                                                                                  |
| org.javadelight:delight-rhino-sandbox                              | 0.2.5                  | 0.2.5              | ✅   |                                                                                  |
| org.javassist:javassist                                            | 3.33.0-GA              | 3.33.0-GA          | ✅   |                                                                                  |
| org.json:json                                                      | 20260814               | 20260814           | ✅   |                                                                                  |
| org.jsoup:jsoup                                                    | 1.23.2                 | 1.23.2             | ✅   |                                                                                  |
| org.lz4:lz4-java                                                   | 1.8.1                  | 1.8.1              | ✅   |                                                                                  |
| org.mongodb:mongo-java-driver                                      | 3.12.14                | 3.12.14            | ✅   |                                                                                  |
| org.mongodb:mongodb-driver                                         | 3.12.14                | 3.12.14            | ✅   |                                                                                  |
| org.mongodb:mongodb-driver-core                                    | 5.13.0                 | 5.13.0             | ✅   |                                                                                  |
| org.mongodb:mongodb-driver-sync                                    | 5.13.0                 | 5.13.0             | ✅   |                                                                                  |
| org.mongodb:bson                                                   | 5.13.0                 | 5.13.0             | ✅   |                                                                                  |
| org.mongodb:mongodb-driver-legacy                                  | 5.13.0                 | 5.13.0             | ✅   |                                                                                  |
| org.mongodb:mongodb-driver-async                                   | 3.12.14                | 3.12.14            | ✅   |                                                                                  |
| org.mozilla:rhino                                                  | 1.9.1                  | 1.9.1              | ✅   |                                                                                  |
| org.msgpack:msgpack-core                                           | 0.9.12                 | 0.9.12             | ✅   |                                                                                  |
| org.msgpack:jackson-dataformat-msgpack                             | 0.9.12                 | 0.9.12             | ✅   |                                                                                  |
| org.mybatis:mybatis                                                | 3.5.19                 | 3.5.19             | ✅   |                                                                                  |
| org.mybatis:mybatis-typehandlers-jsr310                            | 1.0.2                  | 1.0.2              | ✅   |                                                                                  |
| org.openjdk.nashorn:nashorn-core                                   | 15.7                   | 15.7               | ✅   |                                                                                  |
| org.projectlombok:lombok                                           | 1.18.48                | 1.18.48            | ✅   |                                                                                  |
| org.redisson:redisson-spring-data                                  | 4.8.0                  | 4.8.0              | ✅   |                                                                                  |
| org.redisson:redisson-tomcat                                       | 4.8.0                  | 4.8.0              | ✅   |                                                                                  |
| org.redisson:redisson-spring-boot-starter                          | 4.8.0                  | 4.8.0              | ✅   |                                                                                  |
| org.redisson:redisson-mybatis                                      | 4.8.0                  | 4.8.0              | ✅   |                                                                                  |
| org.redisson:redisson                                              | 4.8.0                  | 4.8.0              | ✅   |                                                                                  |
| org.redisson:redisson-all                                          | 4.8.0                  | 4.8.0              | ✅   |                                                                                  |
| org.reflections:reflections                                        | 0.10.2                 | 0.10.2             | ✅   |                                                                                  |
| org.xerial.snappy:snappy-java                                      | 1.1.10.11              | 1.1.10.11          | ✅   |                                                                                  |
| p6spy:p6spy                                                        | 3.9.1                  | 3.9.1              | ✅   |                                                                                  |
| se.svt.oss:media-analyzer                                          | 3.0.0                  | 3.0.0              | ✅   |                                                                                  |
| commons-logging:commons-logging                                    | 1.2                    | 1.4.0              | ⬆️   | ✅ 本轮已升级至 1.4.0                                                            |
| org.apache.tika:tika-core                                          | 3.3.2                  | 4.1.0              | ⬆️   | ✅ 本轮已升级至 4.1.0                                                            |
| org.apache.tika:tika-parsers-standard-package                      | 3.3.2                  | 4.1.0              | ⬆️   | ✅ 本轮已升级至 4.1.0                                                            |
| com.alibaba:transmittable-thread-local                             | 2.14.5                 | 2.14.5             | ✅   |                                                                                  |
| com.alibaba.cloud:spring-cloud-alibaba-dependencies                | 2025.0.0.0             | 2025.1.0.0         | 🔒   | 用户指定 2025.0.0.0                                                              |
| com.alibaba.fastjson2:fastjson2                                    | 2.0.65                 | 2.0.65.android8    | 🔒   | latest 为 android 变体                                                           |
| com.github.chris2018998:beecp                                      | 4.2.0                  | 5.2.3              | 🔒   | dynamic-datasource 绑 beecp 4.x                                                  |
| com.github.pagehelper:pagehelper-spring-boot-starter               | 2.1.1                  | 4.1.1              | 🔒   | 2.x 为 Boot3 终线（官方无 3.x），4.x 属 Boot4 线                                 |
| com.google.guava:guava                                             | 33.7.2-jre             | r09                | 🔒   | r09 为远古 tag 伪影；-jre 线最新                                                 |
| com.mysql:mysql-connector-j                                        | 9.7.0                  | 26.7.0             | 🔒   | 26.x 为新 calver 线；Boot 3.5 绑 9.7.0                                           |
| com.sun.xml.bind:jaxb-impl                                         | 2.3.1                  | 4.0.9              | 🔒   | jakarta 4.x；项目发布 javax shim                                                 |
| com.sun.xml.bind:jaxb-core                                         | 2.3.0.1                | 4.0.9              | 🔒   | 同上                                                                             |
| com.sun.xml.ws:jaxws-ri                                            | 3.0.2                  | 4.0.5              | 🔒   | jakarta 4.x；javax shim                                                          |
| io.github.openfeign:feign-httpclient                               | ~~13.6.1~~ → **13.17** | 13.17              | ⬆️   | 已升级（13.17 全部非破坏性变更）                                                 |
| io.github.openfeign:feign-hc5                                      | ~~13.6.1~~ → **13.17** | 13.17              | ⬆️   | 已升级（13.17 全部非破坏性变更）                                                 |
| io.github.openfeign:feign-okhttp                                   | ~~13.6.1~~ → **13.17** | 13.17              | ⬆️   | 已升级（13.17 全部非破坏性变更）                                                 |
| io.github.openfeign:feign-hystrix                                  | ~~13.6.1~~ → **13.17** | 13.17              | ⬆️   | 已升级（13.17 全部非破坏性变更）                                                 |
| javax.el:javax.el-api                                              | 3.0.0                  | 3.0-b08            | 🔒   | 3.0-b08 为 beta                                                                  |
| javax.mail:mail                                                    | 1.4.7                  | 1.5.0-b01          | 🔒   | 1.5.0-b01 为 beta；1.4.7 为 javax 终版                                           |
| javax.xml.bind:jaxb-api                                            | 2.3.1                  | 2.4.0-b180830.0359 | 🔒   | javax shim；2.4.0-b* 为 beta                                                     |
| net.bytebuddy:byte-buddy                                           | 1.17.8                 | 1.18.14-jdk5       | 🔒   | latest 为 jdk5 特殊变体；Boot 3.5 绑 1.17.8                                      |
| org.apache.seata:seata-rm-datasource                               | 2.5.0                  | 2.6.0              | 🔒   | SCA 对齐 2.5.0                                                                   |
| org.glassfish.jaxb:jaxb-runtime                                    | 2.3.1                  | 4.0.9              | 🔒   | 同上                                                                             |
| org.mybatis:mybatis-spring                                         | 3.0.5                  | 4.1.0              | 🔒   | MP 3.5.17 编译于 3.0.x，4.x 破坏兼容                                             |
| org.mybatis.spring.boot:mybatis-spring-boot-starter                | 3.0.5                  | 4.1.0              | 🔒   | 同上（Boot4 线）                                                                 |
| org.springdoc:springdoc-openapi-starter-webmvc-api                 | 2.8.13                 | 3.1.1              | 🔒   | 3.x 属 Boot4 线                                                                  |
| org.springdoc:springdoc-openapi-starter-webmvc-ui                  | 2.8.13                 | 3.1.1              | 🔒   | 3.x 属 Boot4 线                                                                  |
| org.springdoc:springdoc-openapi-starter-webflux-api                | 2.8.13                 | 3.1.1              | 🔒   | 3.x 属 Boot4 线                                                                  |
| org.springdoc:springdoc-openapi-starter-webflux-ui                 | 2.8.13                 | 3.1.1              | 🔒   | 3.x 属 Boot4 线                                                                  |
| redis.clients:jedis                                                | 6.0.0                  | 8.0.2              | 🔒   | spring-data-redis 3.5 针对 jedis 6 编译；8.x 需随 Boot4/spring-data 升级一并处理 |

## 统计

| 状态                    | 数量    |
|-------------------------|---------|
| ✅ 已最新（含伪影确认） | 166     |
| ⬆️ 本轮升级             | 3       |
| 🔒 受限保持             | 27      |
| **合计**                | **196** |

受限项均可追溯到明确约束：Spring Boot4 生态线（springdoc 3 / pagehelper 4 / mybatis-spring 4）、javax shim
线（jaxb/jaxws/mail）、上游绑定（SCA-seata / dynamic-datasource-beecp / Cloud-feign / Boot-BOM）。

