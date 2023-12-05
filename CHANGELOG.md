# 组件

##### 1.3.1.20231204-RELEASE

* feat: 升级springboot到3.1.6
* feat: 升级相关类库版本
* feat: 删除多余代码、工具类
* feat: 移除`components-starter-compatible-ribbon-to-loadbalancer`模块

##### 1.2.3.20220414-RELEASE

* feat: 升级springboot到2.6.6
* feat: 升级springcloud
* feat: 升级javacv版本
* feat: 升级相关类库版本
* feat: 移除 `validation-api` 迁移至 `jakarta-validator`, 当前仅迁移到2.x版本过渡

##### 1.2.2.20211125-RELEASE

* feat: 增加ProcessUtil工具类
* feat：mongodb、mysql组件包升级、优化
* feat：event 组件包升级、优化，增加callback抽象类

##### 1.2.1.20211025-RELEASE

* feat: 升级框架版本
* feat：去除ribbon
* feat：增加ribbon兼容包

##### 1.1.5.20210114-RELEASE

* feat: 增加WebFlux支持
* feat: 优化参数校验参数校验顺序问题
* feat: 增加 `PdfBoxUtil`
* feat: component-starter-mongodb 支持下划线属性格式
* feat: 对于属性为id的主键，可以不用重写指定的方法
* feat: 增加`ReactiveRequestContextFilter`
* feat: 增加 `MvcSessionUtil` `ReactiveSessionUtil`

##### 1.1.4.20201218-RELEASE

* feat: 增加CollectionResult类型
* feat: 增加MapResult类型
* feat: 增加SetResult类型
* feat: 增加UrlFilterUtil工具类
* feat: 增加google concurrentlinkedhashmap-lru依赖包

##### 1.1.3.20201127-RELEASE

* feat: 增加prometheus 指标库
* feat: 增加maven-archetype-plugin插件库

##### 1.1.2.20201116-RELEASE

* fix: 修复获取ip bug
* feat: 包命名更改
* fix: 去除Utils模块中servlet包
* feat: 添加starter-banner
* feat: 添加starter-database
* feat: 添加starter-mongodb
* feat: 添加skywalking依赖包
* feat: web模块请求异常打印请求参数
* feat: web模块404打印源请求地址
* feat: log4j2线程池disruptor依赖包
* feat: 添加`TextFormatUtil`工具类
* feat: 添加extra-dependencies
* feat: 添加字符验证注解@LengthSize、@NotContainBlank、@NotContainNull
* feat: starter-database模块支持lambdaUpdateWrapper
* feat: 添加 swagger starter 3.0
* feat: 删除 SOFA-boot
* feat: 添加houbb拼音扩展
* feat: spring-support模块整合hutool-support
* feat: 基于@AccessControl的访问控制，动态配置化
* feat: 方法耗时计算切面注解 @ExecuteTimeLog

##### 1.0.1.20200730-RELEASE

* fix: 诸多bug修复
* fix: git地址更改为线上地址
* feat: 请求异常打印请求参数
* feat: 升级依赖包版本
* feat: sentinel 返回值拦截处理
* feat: 线程池工具类增强、添加ComplateFutureUtil异步工具类

##### 1.0.0-RELEASE

* 首次发布










