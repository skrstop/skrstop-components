# 组件

##### 1.3.3.20240510-RELEASE

* fix: 修复@SProcessor注解无法扫描到自身bean的问题
* fix: RegularExpressionConst.HTTP_URL正则表达式优化
* fix: dynamicRedissonClient 启动依赖DsSelector报错，默认关闭动态redissonClient
* fix: object-storage-cos 修复base-path参数失效问题
* fix：object-storage-cos 修复临时资源访问地址错误的问题
* feat：object-storage-cos 增加公共资源访问地址生成方法·getPublicAccessUrl·
* feat: Result增加timestamp字段, 可用于客户端本地时间验证等场景
* fix：Mybatis-plus-join 插件注入bug修复
* fix：使用hutool-bom
* fix: cos临时签名bug修复

##### 1.3.2.20240510-RELEASE

* fix: 修复disruptor包版本问题，4.0.0 降到 3.4.4
* feat: 增加磁盘容量检测工具类DiskSpaceUtil
* feat: components-starter-object-storage增加腾讯cos支持
* feat: components-starter-object-storage增加多数据源支持 @DSObjectStorage
* feat: @SProcessor 支持处理类本身实现断言ProcessorAssert接口
* feat: components-starter-feign-protostuff 增加`入参`支持protostuff序列化
* feat: components-starter-feign-protostuff 增加FeignClient返回值自适应处理
* feat: components-starter-feign-protostuff 增加请求头传递
* feat: components-starter-mongodb 增加多数据源支持
* feat: components-starter-annotation @AccessLimit 增加自定义规则支持

##### 1.3.1.20231221-RELEASE

* feat: 升级springboot到2.7.18
* feat: 升级相关类库版本
* feat: starter-redis增加多数据源支持 @DSRedis
* feat: starter-redis redisson支持多数据源 @DSRedisson
* feat: starter-database 支持多数据源 @DSDatabase
* feat: starter-database 支持多数据源垮库本地事务 @DSDatabaseTransactional
* fix: 修复starter-database自定义mapper地址后，无法扫描默认的SuperMapper
* refactor: 删除多余代码、工具类
* refactor: 命名修改
* refactor: core-common的IResult优化
* refactor: core-exception的Exception优化
* refactor: starter-annotation优化
* refactor: starter-web优化
* refactor: components-starter-compatible-ribbon-to-loadbalancer更名为components-extra-compatible-ribbon-to-loadbalancer
* refactor: 统一Properties配置类bool类型为boolean

