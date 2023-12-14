# 组件

##### 1.2.4.20231128-RELEASE

* feat: 升级springboot到2.7.18
* feat: 升级相关类库版本
* feat: redis组件增加多数据源支持
* fix: 修复starter-database自定义mapper地址后，无法扫描默认的SuperMapper
* refactor: 删除多余代码、工具类
* refactor: 命名修改
* refactor: IResult优化
* refactor: Exception优化
* refactor: starter-annotation优化

##### 1.2.3.20220414-RELEASE

* feat: 升级springboot到2.6.6
* feat: 升级springcloud
* feat: 升级javacv版本
* feat: 升级相关类库版本
* refactor: 移除 `validation-api` 迁移至 `jakarta-validator`, 当前仅迁移到2.x版本过渡

##### 1.2.2.20211125-RELEASE

* feat: 增加ProcessUtil工具类
* perf：mongodb、mysql组件包升级、优化
* perf：event 组件包升级、优化，增加callback抽象类

##### 1.2.1.20211025-RELEASE

* feat: 升级框架版本
* refactor：去除ribbon
* refactor：增加ribbon兼容包
