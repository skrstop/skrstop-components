# components-extra-compatible-morphia-java8

`dev.morphia.morphia:morphia-core:2.5.3` 的 JDK8 重编译版本。

## 背景

上游官方构件 `morphia-core:2.5.3` 为 Java 11 字节码（class file version 55）， 无法在 Java 8 项目中加载。本构件使用 JDK8
对同一份源码重新编译（class file version 52）， 类名、包名与上游完全一致，可直接替代上游构件使用。

## 源码来源

| 项目        | 说明                                                                                       |
|-------------|--------------------------------------------------------------------------------------------|
| 上游        | <https://github.com/MorphiaOrg/morphia>                                                    |
| 本地源码    | `/Users/jphoebe/opt/code/IdeaProjects/github/morphia`                                      |
| 分支        | `2.5.3_11_8`（JDK8 移植线）                                                                |
| 对应 commit | `713aedae0b4`（2026-10-09 15:09:35 +0800，"Merge v2.5.3 into 2.5.3_11_8 (JDK8 backport)"） |
| 二进制      | `lib/morphia-core-2.5.3.jar`（2026-10-09 由上述快照构建，Java-Version: 8）                 |

> 完整源码随本构件的 `-sources.jar` 发布（位于 `src/morphia/java`，不参与编译）。

## 重新编译 lib/morphia-core-2.5.3.jar

```bash
cd /Users/jphoebe/opt/code/IdeaProjects/github/morphia
git checkout 2.5.3_11_8
# 使用 JDK8 构建 core 模块（构建插件已在该分支的 core/pom.xml 中注释掉）
mvn -pl core -am package -DskipTests
cp core/target/morphia-core-2.5.3.jar \
  /Users/jphoebe/opt/code/IdeaProjects/github/skrstop-components-8/components-extra/components-extra-compatible-morphia-java8/lib/
```

## 打包内容

- `lib/morphia-core-2.5.3.jar` 在 `process-resources` 阶段解包进 `target/classes`， 与标记类 `MorphiaCoreJava8` 一起构成本构件的主
  jar
- `-sources.jar`：`src/morphia/java` 下的 morphia-core 完整源码 + 标记类源码
- `META-INF/LICENSE-morphia-core`：上游 Apache License 2.0 许可证，随 jar 发布

## 依赖声明

与上游 `morphia-core` 的 compile 依赖保持一致（见 `pom.xml`）， 版本由 `dev.morphia.morphia:morphia:2.5.3` 的 BOM 与本项目根
POM 共同管理。
