<div align="center">

# skrstop-components

**基于 Spring Boot 3.5 / Spring Cloud 2025 的企业级微服务组件框架**

统一依赖管理 · 开箱即用的 Starter · 全覆盖工具库

![JDK](https://img.shields.io/badge/JDK-17+-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.16-brightgreen)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.3-green)
![Spring Cloud Alibaba](https://img.shields.io/badge/SCA-2025.0.0.0-orange)
![Maven](https://img.shields.io/badge/Maven-3.9+-yellowgreen)
![License](https://img.shields.io/badge/License-Apache%202.0-red)

</div>

---

## ✨ 为什么选择 skrstop-components

一套面向 Spring Boot 3 微服务生态沉淀的组件框架，为业务项目解决三类问题：

### 🎯 统一规范，开箱即用

- **统一返回值与全局异常**：`components-starter-web` 提供全链路一致的 `Result` 响应模型与异常兜底，杜绝各项目各自造轮子
- **统一配置约定**：所有 starter 遵循同一套配置前缀与默认值哲学，接入成本趋近于零
- **统一依赖仲裁**：196 个三方坐标穷举审计、版本受控（见 [docs/upgrade/VERSION-ACCEPTANCE.md](docs/VERSION-ACCEPTANCE.md)
  ），业务项目不再需要操心依赖冲突

### ⚡ 开箱即用的 Starter 能力

- **数据库**：MyBatis-Plus 深度封装（`SuperRepository` 免写 Service/Mapper 的 CRUD）、动态多数据源、分布式事务、分页、ID 生成
- **Redis**：Lettuce/Jedis 动态切换、 **Redisson 动态多实例**客户端（一个客户端管理多套 redis 集群）、分布式锁
- **MongoDB**：动态多数据源 `DynamicMongoClient`，按上下文自动路由
- **Feign**：HTTP/2 + Protostuff 二进制序列化，通信体积与性能显著优于 JSON
- **对象存储**：腾讯 COS / 阿里 OSS / 华为 OBS 统一 API
- **更多**：分布式 ID、端口自动偏移、JVM 监控指标、API 文档（OpenAPI 3.1）

### 🧰 全覆盖工具库

序列化（fastjson2 / gson / jackson / protostuff / protobuf / kryo / msgpack / hessian）、加解密、压缩、HTTP 客户端、线程池（含
TTL 上下文传递）、进程内事件、媒体处理（javacv/FFmpeg 视频、tika
文件识别）、系统探测（oshi）、Emoji/二维码/邮件/验证码……业务代码里的"瑞士军刀"，一处引入处处可用。

### 🔄 平滑升级保障

框架层消化三方库的破坏性变更（如 Redisson 4 API 重构、OkHttp 5 坐标迁移、MyBatis-Plus
包迁移），业务方只需跟随框架版本升级即可，历次升级均有完整迁移文档（[docs/upgrade](docs/upgrade)）。

## 📋 环境要求

* JDK 17
* JDK 17 以下，请使用 1.2.x 版本

## 📦 maven 使用

已上传 maven，请按需使用

```xml
<dependency>
    <groupId>com.skrstop.framework</groupId>
    <artifactId>components-starter-xxxx</artifactId>
    <version>${skrstop.version}</version>
</dependency>
```

> 建议业务项目继承 `components-project` 父 POM，或通过 `dependencyManagement` 导入框架 BOM，以统一三方依赖版本仲裁。

## 🧩 基础工具包

```
skrstop-components
├── components-core                                # 基础核心模块
│   ├── components-core-common                     #   基础实体、统一返回值接口
│   ├── components-core-exception                  #   通用异常、异常接口
│   └── components-core-annotation                 #   通用注解
│
├── components-starters                            # starter 自动配置模块
│   ├── components-starter-common                  #   通用模块
│   ├── components-starter-annotation              #   通用自定义注解（限流、脱敏、处理器容器）
│   ├── components-starter-banner                  #   banner
│   ├── components-starter-database                #   database：MP封装、动态数据源、事务、SuperRepository
│   ├── components-starter-redis                   #   redis：Lettuce/Jedis/Redisson 动态客户端
│   ├── components-starter-mongodb                 #   mongodb：动态多数据源
│   ├── components-starter-feign-protostuff        #   feign 的 http2 和 protobuf 支持
│   ├── components-starter-id                      #   id 服务自动配置
│   ├── components-starter-metrics                 #   额外监控指标支持
│   ├── components-starter-object-storage          #   对象存储服务自动配置（COS/OSS/OBS）
│   ├── components-starter-port-shift              #   端口自动偏移
│   ├── components-starter-spring-support          #   spring 通用支持方法
│   └── components-starter-web                     #   统一返回值、异常处理、API 文档
│
├── components-utils                               # 工具模块
│   ├── components-util-compression                #   解压缩
│   ├── components-util-constant                   #   常量池
│   ├── components-util-crypto                     #   加解密
│   ├── components-util-db                         #   database
│   ├── components-util-event                      #   进程内事件模型
│   ├── components-util-executor                   #   线程池
│   ├── components-util-extra                      #   Emoji、FTP、二维码、邮件、图片验证码
│   ├── components-util-http                       #   http 客户端：JDK HttpClient、OkHttpClient
│   ├── components-util-media                      #   视频处理、图片处理、文件识别
│   ├── components-util-serialization              #   序列化：json、gson、protostuff、kryo 等
│   ├── components-util-setting                    #   属性文件处理
│   ├── components-util-stress                     #   本地压力测试包
│   ├── components-util-system                     #   IP 检测、端口检测、系统参数获取、脚本运行
│   └── components-util-value                      #   值处理：bean/string/array/collection/date/reflect...
│
├── components-examples                            # 样例工程（含集成测试）
│   ├── components-example-starters-cloud-feign    #   feign 样例
│   └── components-example-starters-simple         #   其他简单样例
│
├── components-project                             # 业务项目父 pom 模块
├── components-extra                               # 额外扩展模块
├── docs/upgrade                                   # 版本升级指南与依赖验收报告
├── CHANGELOG.md                                   # 版本发布日志
└── pom.xml                                        # 依赖包、协议、署名等管理
```

## 📚 核心三方依赖版本

| 依赖                 | 版本       | 依赖            | 版本          |
|----------------------|------------|-----------------|---------------|
| Spring Boot          | 3.5.16     | MyBatis-Plus    | 3.5.17        |
| Spring Cloud         | 2025.0.3   | Redisson        | 4.8.0         |
| Spring Cloud Alibaba | 2025.0.0.0 | Lettuce / Jedis | 6.6.0 / 6.0.0 |
| springdoc-openapi    | 2.8.13     | MongoDB Driver  | 5.13.0        |
| OpenFeign            | 13.17      | Hutool          | 5.8.48        |
| Seata                | 2.5.0      | fastjson2       | 2.0.65        |
| MyBatis              | 3.5.19     | Druid           | 1.2.28        |

> 全部依赖已完成穷举式版本审计，明细见 [docs/VERSION-ACCEPTANCE.md](docs/VERSION-ACCEPTANCE.md)

## 📖 文档

| 文档                                                             | 说明                                                     |
|------------------------------------------------------------------|----------------------------------------------------------|
| [docs/upgrade](docs/upgrade)                                     | 按版本拆分的升级指南（破坏性变更、代码适配、迁移建议）   |
| [docs/upgrade/VERSION-ACCEPTANCE.md](docs/VERSION-ACCEPTANCE.md) | 196 个三方依赖的版本验收对照表（当前版本 vs repo1 最新） |
| [CHANGELOG.md](docs/CHANGELOG.md)                                | 版本发布日志                                             |

## 🗺️ 计划待添加功能

* [ ] components-starter-object-storage 模块中增加：阿里云oss、华为云obs
* [ ] components-starter-mqtt
