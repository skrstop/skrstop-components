# 组件

##### 3.5.100.20261009-RELEASE

* upgrade: springBoot 3.3.13 -> 3.5.16、springCloud 2023.0.6 -> 2025.0.3、springCloudAlibaba 2023.0.3.4 -> 2025.0.0.0
* upgrade: redisson 3.43.0 -> 4.8.0（移除 getNodesGroup，Config.fromJSON 兼容层 RedissonConfigCompat）、 okhttp 4 ->
  okhttp-jvm 5.5.0、mybatis-plus 3.5.7 -> 3.5.17（MybatisSqlSessionFactoryBean 包迁移、批量回调签名 Integer）
* fix: DynamicAopRedissonClient getBoundedBlockingQueue/getDeque 递归自调用 StackOverflowError 修复
* fix: SuperRepositoryImpl#saveOrUpdateBatch 改用虚分派 getSqlSessionFactory ()，子类覆写生效
* refactor: 约 40 处不可达防御分支/死代码等价重构（trim/annotation/redis/feign/media/object-storage 等）
* feat: 全仓 JaCoCo 覆盖率 100%（1514 tests，0 失败/0 跳过），verify 阶段 LINE/BRANCH=1.00 构建门禁，移除全部 excludes
* docs: 新增升级指南 docs/upgrade/3.5.100.20261009.md

##### 1.3.5.20260520-RELEASE

* feat: druid usePingMethod支持通过yml配置指定，默认为false
* feat: 去除主键id检查

##### 1.3.4.20250930-RELEASE

* refactor: 重构database、mongodb组件重构

##### 1.3.3.20241112-RELEASE

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
* fix: StrUtil.format方法bug修复
* feat: components-starter-object-storage cos 临时访问签名增加数据万象处理参数
* refactor: 合并data和pageInfo, 统一放在data中
* fix: starter-web兼容springdoc的接入

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

