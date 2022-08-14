# Monet SkyWalking Java Agent
## version 8.9.0

## 自定义插件
### monet-log-plugin
- 适配 Logback 链路
- 适配 Log4j2 链路
- 适配 ```ThreadPoolExecutor.execute``` Lambda 表达式
- 适配 ```ThreadPoolExecutor.submit``` Lambda 表达式

## 构建

### 中文社区指引
> https://skyapm.github.io/document-cn-translation-of-skywalking/zh/8.0.0/guides/How-to-build.html#
### Apache 发行版本源代码构建
#### macOS 10.13.6 (MacBook Pro 2018 - Intel)
- 修改 ```apache-skywalking-java-agent-8.9.0/pom.xml```
    - 注释 ```<grpc.version>1.44.0</grpc.version>``` 改为命令注入 ```version```
    - 注释 ```<protoc-gen-grpc-java.plugin.version>1.44.0</protoc-gen-grpc-java.plugin.version>``` 改为命令注入 ```version```
    - 注释 ```plugin``` -> ```maven-checkstyle-plugin```
```
# 构建命令
./mvnw -Dgrpc.version=1.43.0 -Dprotoc-gen-grpc-java.plugin.version=1.43.0 clean package -DskipTests
```

#### Windows10 (19044.1826 - Intel)
- 修改 ```apache-skywalking-java-agent-8.9.0/pom.xml```
    - 注释 ```<grpc.version>1.44.0</grpc.version>``` 改为命令注入 ```version```
    - 注释 ```<protoc-gen-grpc-java.plugin.version>1.44.0</protoc-gen-grpc-java.plugin.version>``` 改为命令注入 ```version```
    - 注释 ```plugin``` -> ```maven-checkstyle-plugin```
```
# 构建命令
# Terminal -> powershell
./mvnw '-Dgrpc.version=1.43.0' '-Dprotoc-gen-grpc-java.plugin.version=1.43.0' clean package -DskipTests
```

## 跨线程
- 适配 ```ThreadPoolExecutor.execute``` Lambda 表达式
- 适配 ```ThreadPoolExecutor.submit``` Lambda 表达式
### 使用方式
1. 构建 ```skywalking-agent/```
2. 移动 ```skywalking-agent/bootstrap-plugins/apm-jdk-threading-plugin-8.9.0.jar``` 至 ```skywalking-agent/plugins/apm-jdk-threading-plugin-8.9.0.jar```
3. 服务启动命令 指定 ```-Dskywalking.plugin.jdkthreading.threading_class_prefixes=[YOUR_APPLICATION_ROOT_PACKAGE]```
