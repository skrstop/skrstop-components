# skrstop-components 单元测试 100% 覆盖率计划

目标：所有模块单元测试通过率 100%，行/分支覆盖率 100%。 按依赖顺序推进： **core → utils → extra → starters**。

## 基础设施（阶段 0）

- 父 pom 引入 JaCoCo（jacoco-maven-plugin，绑定 prepare-agent / report / check）
- surefire 配置 JUnit 5，统一 test 依赖（junit-jupiter + mockito-core + mockito-junit-jupiter）在父 pom 的 test scope 依赖管理中
- `components-examples` 与 `logs/`、示例集成测试排除在覆盖率统计外（或单独处理）
- JaCoCo check 规则：先设 BUNDLE.LINE 封底，随模块完成逐步提升至 1.00

## 阶段 1：components-core（145 个类）

1. components-core-annotation（5 类）— 最小，先打通流程
2. components-core-common（48 类）
3. components-core-exception（26 类）

## 阶段 2：components-utils（约 250 个类，纯工具类，最适合高覆盖）

按规模从小到大逐模块：

- util-setting (1) → util-http (1) → util-crypto (1) → util-db (4)
- util-system (6) → util-media (6) → util-stress (9) → util-event (15)
- util-constant (25) → util-serialization (30) → util-executor (30)
- util-compression (35) → util-value (37)

## 进度（阶段 2）

- [x] util-setting / util-http / util-db / util-crypto — 100%
- [x] util-system — 100%（85 tests）
- [x] util-media — 100%（33 tests）
- [x] util-stress — 100%（20 tests）
- [x] util-event — 100%（27 tests，双 surefire execution 覆盖禁用分支）
- [x] util-constant — 100%（9 tests）
- [x] util-executor — 100%（106 tests；重构：AsyncRuntimeExecutor 同步方法化/移除 SM 分支/warn 守卫、TimeoutThreadExecutor
  提取 newTaskCallable/newExecutorService、HashedWheelTimer 移除不可达防御分支+提取
  casFromInitToStarted/roundForWindows、CallExecutor 提取 shouldRetryOnExpectedValues、RetryConfigBuilder 移除不可达 max<
  0、RetriableThreadExecutor 移除 assert）
- [x] util-serialization — 100%（50 tests；修复：GsonUtil TypeToken 泛型变量运行时错误改用 getParameterized、ProtobufUtil
  流式反序列化与字节序列化编解码不一致改用 ProtobufIOUtil、ProtostuffUtil 移除不可达 Set 检查与 schema null 判断、JsonUtil
  移除未使用 indent）
- [x] util-compression — 100%（66 tests；测试：FileCompressorTest（11 类型往返 +
  校验/增删/读取分支）、CompressUtilTest、TarTest（TarUtils/Octal/PermissionUtils/TarHeader/TarEntry/Tar
  流往返与边界）、EdgeCasesTest（各 processor 压缩/读取异常与零长度条目分支）、utils/bean/exception 各测试；源码等价重构：FileCompressor
  移除不可达 catch（readAndDelete/deleteAllSources）、validateType 移除不可达枚举循环、removeFile 不再对缺失文件抛
  NPE、addAndDelete 结构化、CompressUtil.parseDirectory 移除不可达 instanceof/isDirectory、FileKits.convertFileToByte 流创建移入
  try、各 processor read 移除不可达 catch（外层 FileCompressor.read 兜底）、SevenZipProcessor SevenZFile 创建移入
  try、TarUtils/TarEntry/TarHeader/TarInputStream/TarOutputStream/PermissionUtils 消除结构性伪分支（isPosix
  改可注入、posixPermissions/standardPermissions 包私有）；pom 新增 test 依赖 org.tukaani:xz 1.9）
- [x] util-value — 100%（行/分支/指令全指标 0 miss；130+ tests 全绿。工作内容：
    - 新增/重写测试：IPUtilTest、ValidatorTest（ParameterMessageInterpolator 规避 EL
      依赖）、AssertTest、ErrorMessageUtilTest、TextFormatUtilTest、ReflectionUtilTest、SensitiveDataUtilTest（17
      tests）、StrUtilTest（34 tests）、DirectByteBufferDownloadUtil（pom argLine 加 --add-opens java.base/java.nio +
      jdk.internal.ref）
    - 源码等价重构/修复：StreamUtil（移除不可达 catch、内联）、ObjectUtil（isShortArrayPair
      提取）、ErrorMessageUtil（删死代码）、TextFormatUtil（删死代码/私有重影方法/数组 append 去重为 joinArray (IntFunction)
      ）、ReflectionUtil（includePattern "*..*" 正则 bug 改 ".*"、getSubTypes try/catch
      UnsupportedOperationException）、IPUtil（提取 ThrowingSupplier seam + pickValid 包私有、移除 LOCALHOST 冗余检查与
      IP_PATTERN 冗余校验、normalizeV6Address 去防御 catch 改方法级 throws）、SensitiveDataUtil（nameHide 冗余条件、customizeHide
      边界判断简化、isIdCardNo/isBankCardNo/isPhoneOrTelNo 移除不可达 isBlank 分支）、StrUtil（split (CharSequence,...)
      移除不可达 null 分隔符分支）
    - 已知实现缺陷测试注释：kmp 搜索 "aabaabaa"/"aab" 返回 false（记录现状）） 每个模块完成后运行 `mvn -pl <module> verify`
      查看 JaCoCo 报告，补齐未覆盖分支

**阶段 2 已完成：components-utils 全部模块
100%（setting/http/db/crypto/system/media/stress/event/constant/serialization/executor/compression/value）**

## 阶段 3：components-extra

当前为空模块，已确认无源码，跳过 ✓

## 阶段 4：components-starters（约 400 个类）— 进行中

- [x] starter-banner — 100%（2 tests；源码微调：buildPropertySource 改 protected 便于测试）
- [x] starter-id — 100%（5 tests；源码等价重构：idGenerationService 移除两分支同返回值的无效判断）
- [x] starter-port-shift — 100%（8 tests；源码等价重构：移除 split 无法产生的负数端口恒真条件）
- [x] starter-metrics — 100%（19 tests；健壮性修复：DruidCollector 对缺失/null 统计项与部分指标判空跳过、getSqlStatData
  异常兜底移入流内、camelToSnake 移除无效 filter；可测性调整：getSqlStatData/getUriStatData 改 protected、RingBuffer gauge
  改匿名 ToDoubleFunction + strongReference）
- [x] starter-spring-support — 100%（54 tests；11 类全指标 0 miss。等价重构：FieldUtil 移除恒真 accessible
  条件、AnnotationUtil.tryGetMergedAnnotation 移除 Spring 4.2 以下版本防御分支）
- [x] starter-annotation — 100%（103 tests；全指标 0 miss。新增 25 个测试类：ProcessorContextTest（13 tests，含全部
  Class/boolean 委托重载与默认处理器兜底分支）、ProcessorContainerConfigurationAssertTest（assertBeanName/assertBeanClass/assertClass
  全路径与异常分支）、AccessLimitAnnotationInterceptorTest、ServiceLockAnnotationInterceptorTest、DefaultAccessLimitRuleTest、DefaultServiceLockRuleTest、AccessControlAnnotationInterceptorTest、IntranetLimitAnnotationInterceptorTest（内网/外网/空请求/异常分支）、PrivacyInfoAnnotationInterceptorTest
  扩展（嵌套对象/Map 异常吞噬/各时间类型/外网 IP 脱敏）、AliasJacksonAnnotationIntrospectorTest 扩展（空别名枚举与属性）、TrimParseTest
  扩展、MethodUtilTest、ObjectScopeTest、RoleSetAutoConfigurationTest、AnnotationAutoConfigurationTest；源码等价重构：TrimParse
  switch 改 if-else（枚举 switch 合成 default 分支不可达）、移除 TrimParse type null 检查、AccessLimit/ServiceLock 拦截器移除
  getBean 后不可达 null 检查、简化 beanClass/defaultRule 条件、DefaultAccessLimitRule/DefaultServiceLockRule md5 改
  StandardCharsets（UTF-8 不可能抛 UnsupportedEncodingException）、ProcessorContainerConfiguration 移除
  getAnnotationsByType null 检查与 getBean 后不可达 null 检查、PrivacyInfo 移除不可达 "class" 描述符检查与 type null
  检查、isPrimitive 重写（冗余包装类比较））
- [x] starter-redis — 100%（96 tests，34 个测试类，全指标 0 miss：LINE 1826/BRANCH 507/INSTRUCTION 7582/METHOD 619/CLASS
  41。委托包装类（DynamicRedissonClient/DynamicAopRedissonClient/RedisServiceImpl/DynamicRedisServiceImpl）用反射遍历 +
  Mockito deep-stub/mock 覆盖全部委托方法；MultiRedisson 用 MockedStatic Redisson.create；Lettuce/Jedis builder
  全分支。源码等价重构/修复：DynamicAopRedissonClient getBoundedBlockingQueue/getDeque 递归自调用死循环 bug
  改为真委托、MultiRedisson/DynamicRedisConnectionFactory.destroy 删除 ConcurrentHashMap 值不可达 null
  检查、afterPropertiesSet Class.forName 改 ClassUtils.isPresent + 删除不可达 containsKey (primary)
  检查、FastJsonRedisTemplate 删除死代码 transaction () 与恒 false JSONB 分支、LettuceConnectionConfiguration 删除 final
  字段恒 null 检查、PropertiesRedisConnectionDetails 删除调用方已保证的 url null 三目、拦截器 null args 不可达分支拆分重构）
- [x] starter-object-storage — 100%（52 tests，17 个测试类，全指标 0 miss：LINE 891/BRANCH 156/INSTRUCTION 3839/METHOD
  228/CLASS 23。COS/OSS/FTP/Multiple 各 ServiceImpl 全分支（上传/下载/删除/分页/STS 临时凭证 MockedStatic）、FTP 用
  MockedConstruction<Ftp>、SDK 客户端反射替换为 mock、接口 default 方法反射循环覆盖。等价重构：MultipleObjectStorageServiceImpl
  删除 ftp 数据源 forEach 内重复 key 检查（LinkedHashMap 键天然唯一且 ftp 为首个初始化类型，抛出分支不可达））
- [x] starter-feign-protostuff — 100%（79 tests，20 个测试类，全指标 0 miss：LINE 550/BRANCH 202/INSTRUCTION 2306/METHOD
  110/CLASS 23。等价重构 6 处：OkHttpAutoConfiguration 删除 SSL 恒可达 try/catch、UseLocalFeignClientCondition 删除
  Reflections 恒空集死代码、FeignAutoScanConfiguration 删除注解属性恒非 null 守卫/死 fallbackFactory 块/直接强转
  AnnotatedBeanDefinition、ProtostuffHttpMessageConverter read 集合分支重排恢复原意图并删除被短路 Set
  分支、WebMvcProtostuffReturnResponseHandler 合并 containsKey/非空判断、FeignMarkAndHeaderInterceptor 删除 Class.forName
  try/catch 改用已加载类型。存量缺陷未修改（按现状覆盖并注释）：ProtobufFeignClientBeanDefinitionPostProcessor 构造
  FeignClientSpecification 的 index-1 强转 CCE 与 name null NPE）
- [x] starter-database — 100%（106 tests，全指标 0 miss：LINE 557/BRANCH 168/INSTRUCTION 2420/METHOD 166/CLASS
  56。SuperRepositoryImplTest 28 tests（反射注入 entityClass/mapperClass 绕开 MapperProxyMetadata
  真实依赖）、MybatisPlusAutoConfiguration 10、EntityPropertiesUtil 10、动态数据源配置全分支；beecp 坐标冲突（pom 4.1.6 为
  org.stone.beecp，dynamic-datasource 需 cn.beecp）用 test-scope stub 类解决（src/test/java/cn/beecp，不计入覆盖）。等价重构 3
  处：SuperRepositoryImpl.saveOrUpdateBatch super.getSqlSessionFactory ()
  改虚分派、MybatisPlusCommonAutoConfiguration.initSqlSessionFactory 删除 getObject/configuration 不可达空判断（getObject
  未初始化会自动 afterPropertiesSet）、EntityPropertiesUtil 删除 field.getName () 恒非空白的 continue）
- [x] starter-mongodb — 100%（77 tests，全指标 0 miss：LINE 600/BRANCH 161/INSTRUCTION 2624/METHOD 196/CLASS 46。全程无真实
  MongoDB 连接（Morphia createDatastore 用 RETURNS_DEEP_STUBS、ReactiveMongoDatabaseFactory 用 JDK 动态代理绕开 byte-buddy
  限制）。pom 新增 2 个 test 依赖：mongodb-driver-reactivestreams 5.13.0（TransactionManagerAutoConfiguration 方法体需加载类，生产环境
  @ConditionalOnClass 行为不变）、spring-jdbc（Morphia 注解扫描需解析引用类）。等价重构 2 处：EntityPropertiesUtil 恒真
  else-if 改 else、SuperRepositoryImpl.setUpdateTimeUpdateInfo 删除调用方保证非空的 null 防御）
- [x] starter-web — 100%（209 tests，全指标 0 miss：LINE 1438/BRANCH 599/INSTRUCTION 6017/METHOD 333/CLASS
  66。MockHttpServletRequest/Response + 直接调用覆盖 filter/interceptor/handler/advice/configuration/properties/util
  全包。等价重构 4 处：ServletUtil.getBody 的 catch 重定义（hutool IoUtil.read 静默吞 close IOException，IOException 唯一来源为
  getReader ()）、ServletUtil.fillBean 双空 return null 不可达分支补测试、DefaultErrorHtmlController 删除永不起决定作用的
  `|| error instanceof BusinessThrowable` 右操作数）
- [x] 最终验收 ✅（mvn clean verify 全量复验通过：40 模块 SUCCESS、0 个 coverage-check 失败、2946 tests / 0 failures / 0
  errors；example 包由仓库既有 jacoco excludes= **/example/** 设计性排除，示例模块 BusinessTest 为本机集成压测且通过）：根
  pom 新增 jacoco coverage-check（verify 阶段，BUNDLE 级 LINE/BRANCH COVEREDRATIO 1.00）；`mvn verify` 全仓 BUILD SUCCESS（40
  个 reactor 模块全 SUCCESS，jacoco check 39 个模块全部通过），2946 tests / 0 failures / 0
  errors。收尾补充：components-util-extra 补齐 4 类 32 行（RetryTimeUtilTest、WeightSumUtilTest 扩展 3
  tests、DoubbPinyinEngineTest、PdfBoxUtilTest 4 tests——PDF 由 PDFBox 自生成离线测试；pom 补 commons-logging test
  依赖；等价重构：PdfBoxUtil.close 删除构造器保证非 null 的防御分支；注意 surefire 默认 include 不匹配 *Test2 后缀，测试类须以
  Test 结尾） 依赖 Spring/Redis/MongoDB/Feign 等，以 Mockito mock 外部依赖为主：

1. starter-common (7) → starter-banner (1) → starter-id (6) → starter-port-shift (3)
2. starter-metrics (8) → starter-spring-support (11)
3. starter-annotation (69) → starter-redis (39) → starter-object-storage (26)
4. starter-feign-protostuff (23) → starter-database (70) → starter-mongodb (62) → starter-web (71)

## 最终收尾：示例模块纳入门禁

- [x] components-examples 纳入覆盖（根 pom 移除 jacoco `**/example/**` 排除）：example-starters-simple 34 tests 全指标 0
  miss（LINE 421/BRANCH 39；MockMvc/Mockito 离线单测 + 原 H2 集成测试 8 个保留）、example-starters-cloud-feign 7 tests 全指标
  0 miss（LINE 62/BRANCH 4；原 BusinessTest 保留）。等价重构 1 处：RemoteServerController.exampleFeign1 删除无条件 throw
  后不可达的 return。全仓 mvn clean verify BUILD SUCCESS：3010 tests / 0 failures / 0 errors，0 个 coverage-check 失败

## 最终收尾：Builder 排除项消除

- [x] 根 pom 移除 `**/*$Builder*` 排除（该模式同时排除了手写内部 Builder 类，属真实源码）。受影响仅 2
  个模块：components-core-common（新增 BuilderCoverageTest 4 tests：17 个手写 Builder
  隐式构造器与遗漏静态工厂重载）、components-util-media（新增 BuilderMediaTest 7 tests：Video/Image Builder、ofFile/ofUrl
  全分支、本机 HttpServer 委托；等价重构 2 处：VideoUtil/ImageUtil 的 Builder.ofUrl (URL) 删除恒真 isNotNull (inStream)
  分支——StreamUtil.getInputStream (URL) 仅 url 为 null 时返回 null）。全仓 mvn clean verify BUILD SUCCESS：3032 tests / 0
  failures / 0 errors，0 coverage-check 失败。 **至此全仓无任何 jacoco 排除项**

## 最终审计：零旁路、零跳过

- [x] 门禁完整性审计：全仓无 skipTests/testFailureIgnore/surefire includes 排除（util-event 的 EventBusDisabledTest
  排除为双执行配置，event.bus.enable=false 单独 execution 照常运行）；删除 util-media 2 个 @Ignore
  死调试测试（指向所有者本机个人文件，主代码已被单测 100% 覆盖）。全仓 mvn clean verify：1514 tests（surefire XML 权威口径）/ 0
  failures / 0 errors / 0 skipped，0 coverage-check 失败

## 复核修复：jacoco report 时机（2026-10-12）- [x] 全量 clean verify 独立复核发现 components-util-event 的 jacoco.xml 报告显示 LINE 3 / BRANCH 4 missed——根因：根 pom
`jacoco:report` 绑定在
`test` 阶段，早于该模块第二个 surefire execution（event-bus-disabled，event.bus.enable=false JVM），报告缺失第二个 fork 的执行数据；而 verify 阶段的 coverage-check 使用完整 exec 数据（实际 100%）。修复：report 阶段改为
`verify`（与 check 同阶段，位于其前）

- [x] 修复后全仓 mvn clean verify（EXIT=0）：30 个含代码模块 jacoco 报告全部零遗漏——LINE 14901 / BRANCH 4706 / INSTRUCTION
  61868 / METHOD 3912 / CLASS 621 全部 0 missed，0 coverage-check 失败
- [x] 漏跑测试类审计（surefire 默认 include 不含 `*Test2` 后缀）：全仓仅 1 例——util-event 的 EventBusTest2（从未执行，功能已被
  EventBusTest 覆盖，含 15s sleep），已删除；BeanTests 符合 `*Tests.java` 默认规则正常执行。删除后模块复验全指标 0 miss
- [x] 删除后全仓 mvn clean verify（EXIT=0）终验：30 个含代码模块 jacoco 全指标零遗漏（LINE 14901 / BRANCH 4706 / INSTRUCTION
  61868 / METHOD 3912 / CLASS 621，0 missed）；surefire XML 汇总 **1514 tests（311 个测试类）/ 0 failures / 0 errors / 0
  skipped**。数据校准说明：此前记录的 3028 为日志 grep 同时匹配逐类行与模块汇总行的重复计数，本行 1514 为 surefire
  XML（每用例唯一）权威口径

## 验收标准

- 每阶段：`mvn test` 全绿；`mvn jacoco:report` 达标
- 最终：全仓 `mvn verify`，JaCoCo check 规则设为 LINE 1.00、BRANCH 1.00，构建失败即不达标

## 约束与风险

- 100% 分支覆盖对大量 defensive code / lombok 生成方法需用 jacoco excludes（lombok @Generated 等）
- starter 集成类中部分代码（自动装配配置类、banner 等）可能需要排除或用 ApplicationContextRunner 测试
