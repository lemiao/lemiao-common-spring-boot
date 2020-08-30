# Lemiao-Common-Spring-Boot，一个开箱即用的基础应用配置起步依赖项目

- 默认开启使用 HttpClient 作为 RestTemplate 连接工具，支持配置 Http 连接池参数
- 默认开启配置异步线程池，支持配置线程池参数
- 默认开启 DingDingClient 配置
- 支持开启 Leaf Api，支持获取雪花算法ID、号段ID，支持注解使用 Mybatis Plus 生成ID
- 支持开启应用启动通知多个钉钉群机器人消息
- 未完待续

# 使用说明

#### 引入依赖

```xml
<dependency>
    <groupId>com.lemiao.common</groupId>
    <artifactId>lemiao-common-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

#### 应用配置

```c
## 服务配置
spring.application.name=app
spring.profiles.active=dev

## http连接池配置
http-pool.max-total=20
http-pool.default-max-per-route=10
http-pool.connect-timeout=1000
http-pool.connection-request-timeout=3000
http-pool.socket-timeout=5000
http-pool.validate-after-inactivity=7000
http-pool.async=false

## 异步线程池配置
thread.pool.core-pool-size=10
thread.pool.max-pool-size=20
thread.pool.keep-alive-seconds=600
thread.pool.queue-capacity=200
thread.pool.thread-name-prefix=async-thread-

## Leaf服务配置
leaf.server.url=http://localhost:8080

## 应用启动钉钉通知配置
notice.start-notice-urls[0]=https://oapi.dingtalk.com/robot/send?access_token=token1
notice.start-notice-urls[1]=https://oapi.dingtalk.com/robot/send?access_token=token2

```

#### 使用方式

1. 使用 RestTemplate

```java
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRestTemplate() {
        // 
    }

}
```

2. 使用 DingDingClient

```java
public class DingDingClientTest {

    @Autowired
    private DingDingClient dingDingClient;

    @Test
    public void testRestTemplate() {
        // 
    }

}
```

3. @Async异步方法实现

```java
public interface HelloService {

    /**
     * 异步sayHello
     *
     * @param name caller name
     */
    void sayHelloAsync(String name);

}

public class HelloServiceImpl implements HelloService {

    /**
     * 异步sayHello
     *
     * @param name caller name
     */
    @Async(value = "threadPool")
    @Override
    public void sayHelloAsync(String name) {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            log.error("say hello exception", e);
        }
        log.info("service say hello " + name + "!");
    }

}
```

4. 获取异步线程池

```java
public class ExecutorTest {

    /**
     * 注入异步线程池
     */
    @Resource(name = "threadPool")
    private Executor executor;

    @Test
    public void testExecute() {
        // 
    }

}
```

5. 手动获取 Leaf Id

```java
public class LeafApiTest {

    @Autowired
    private LeafApi leafApi;

    /**
     * 获取Leaf号段ID
     */
    @Test
    public void testGetSegmentId() {
        Long id = leafApi.getSegmentId("bizKey");
        System.out.println(id);
    }

    /**
     * 获取Leaf雪花算法ID
     */
    @Test
    public void testGetSnowflakeId() {
        Long id = leafApi.getSnowflakeId("bizKey");
        System.out.println(id);
    }

}
```

6. Mybatis-Plus项目，使用注解方式自动获取 Leaf Id

```java
/**
 * 获取Leaf号段ID
 */
@LeafId(key = "bizKey", idType = LeafIdType.SEGMENT)
public class Entity implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

}

/**
 * 获取Leaf雪花算法ID
 */
@LeafId(key = "bizKey", idType = LeafIdType.SNOWFLAKE)
public class Entity implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

}
```