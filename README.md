# 最低jdk版本

* JDK 17

# 基础工具包

```

├── components-core                                                         #├── 基础核心模块
│   ├── components-core-common                                               │   ├── 基础实体、接口
│   ├── components-core-exception                                            │   ├── 通用异常、异常接口
│   └── components-core-annotation                                           └───└── 通用注解
│
│
├── components-example                                                      #├── 样例
│   ├── components-example-starter-cloud-feign                               │   ├── feign样例
│   └── components-example-starter-simple                                    └───└── 其他简单样例
│
│
├── components-project                                                      #├── 业务项目父pom文件模块
│
│
├── components-extra                                                        #├── 额外模块
│
│
├── components-starters                                                     #├── starter自动配置模块
│   ├── starter-common                                                       │   ├── 通用模块
│   ├── starter-annotation                                                   │   ├── 通用自定义注解
│   ├── starter-banner                                                       │   ├── banner
│   ├── starter-database                                                     │   ├── database
│   ├── starter-feign-protostuff                                             │   ├── feign的http2和protobuf支持
│   ├── starter-id                                                           │   ├── id服务自动配置
│   ├── starter-metrics                                                      │   ├── 额外监控指标支持
│   ├── starter-mongodb                                                      │   ├── mongodb
│   ├── starter-object-storage                                               │   ├── 对象存储服务自动配置
│   ├── starter-port-shift                                                   │   ├── 端口自动偏移
│   ├── starter-redis                                                        │   ├── redis封装方法服务自动配置
│   ├── starter-spring-support                                               │   ├── spring通用支持方法
│   └── starter-web                                                          └───└── 统一返回值、异常处理
│
│
├── components-utils                                                        #├── 工具模块
│   ├── util-compression                                                     │   ├── 解压缩
│   ├── util-constant                                                        │   ├── 常量池                      
│   ├── util-cryto                                                           │   ├── 加解密
│   ├── util-db                                                              │   ├── database
│   ├── util-event                                                           │   ├── 进程内事件模型
│   ├── util-executor                                                        │   ├── 线程池
│   ├── util-extra                                                           │   ├── Emoji、FTP、二维码、邮件、图片验证码
│   ├── util-http                                                            │   ├── http客户端：JDKHTTPClient、OKHttpClient
│   ├── util-media                                                           │   ├── 视频处理、图片处理、文件识别
│   ├── util-serialization                                                   │   ├── 序列化：json、gson、protostuff
│   ├── util-setting                                                         │   ├── 属性文件处理
│   ├── util-stress                                                          │   ├── 本地压力测试包
│   ├── util-system                                                          │   ├── IP检测、端口检测、系统参数获取、脚本运行
│   └── util-value                                                           └───└── 值处理：bean、string、array、collection、number、date、object、lambda、reflect、stream、assert
│
├── LICENSE                                                                 #├── 版权协议
├── README.md                                                               #├── 项目描述文件
├── CHANGELOG.md                                                            #├── 版本发布日志
└── pom.xml                                                                 #├── 依赖包、协议、署名等管理

```

# 常用可用类库版本

* springBoot 3.1.6
* springCloud 2022.0.4
* springCloudAlibaba 2022.0.0.0
* log4j 2.22.0
* lombok 1.8.30
* mybatis 3.5.14
* mybatis-plus 3.5.4.1
* mybatis-fluent 1.9.9
* redisson 3.25.0
* druid 1.2.20
* hutool 5.8.23
* fastjson 2.0.42
* dubbo 3.2.9
* protostuff 1.8.0
* lettuce 6.3.0.RELEASE
* jedis 5.1.0
* morphia 2.4.9
* javacv 1.5.9
* ...

# 计划待添加功能

* lambda func优化改进
* components-starter-database 增加多数据源支持
* components-starter-mongodb 增加多数据源支持
* components-starter-object-storage 模块中增加：阿里云oss、华为云obs、腾讯云cos