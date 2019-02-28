CombinedMq
=====================
[![Build Status](https://travis-ci.com/combinedmq/combinedmq.svg?branch=master)](https://travis-ci.com/combinedmq/combinedmq)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.combinedmq/combinedmq.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.combinedmq%22%20AND%20a:%22combinedmq%22)

[English](https://github.com/combinedmq/combinedmq/blob/master/README.md)

CombinedMq是一个抽象封装了RabbitMq、ActiveMq、Kafka的消息队列组件。

## 使用步骤
如果您使用了Spring相关框架，您可以直接使用combinedmq-spring-boot-starter或combinedmq-spring。
- Maven依赖：

```xml
<dependency>
    <groupId>com.github.combinedmq</groupId>
    <artifactId>combinedmq</artifactId>
    <version>1.0.0</version>
</dependency>
```
- 配置文件——在资源根目录下添加combinedmq.yml文件，如果您使用rabbitmq，可以在yml文件中加入下面内容：
 

```yaml
rabbitmq:
  host: 10.1.7.22
  port: 5672
  username: xiaoyu
  password: xiaoyu
  virtualHost: /
  consumerListener:
    concurrency: 5 #消费者数量
  producerPool: #连接池配置，连接池只是针对生产者有效
    maxTotal: 100
    maxIdle: 20
    minIdle: 10
    maxWaitMillis: 30000
    minEvictableIdleTimeMillis: 60000
    timeBetweenEvictionRunsMillis: 30000
    testOnBorrow: false
    testOnReturn: false
    testWhileIdle: true
# consumerListener和producerPool可以不同时存在
```
其他配置可以查看此[配置文件](https://github.com/combinedmq/combinedmq/blob/master/src/test/resources/combinedmq.yml)。

- 生产者代码实现

```java
public class ProducerTest {
    public static void main(String[] args) throws MqException {
        ConfigurationFactory configurationFactory = new RabbitMqConfigurationFactory();
        ConnectionFactory connectionFactory = new RabbitMqConnectionFactory(configurationFactory.getConfiguration());

        Producer producer = new RabbitMqProducer(connectionFactory);
        producer.send(new RabbitMqQueue("x.y.z"), new RabbitMqMessage("这是一条rabbitmq测试消息".getBytes()));
        producer.send(new RabbitMqQueue("x.y.z"), new RabbitMqMessage("这是一条rabbitmq测试延时消息".getBytes(), 60000L));
    }
}
```

- 消费者代码实现

```java
public class ConsumerTest {
    public static void main(String[] args) throws MqException {
        ConfigurationFactory configurationFactory = new RabbitMqConfigurationFactory();
        ConnectionFactory connectionFactory = new RabbitMqConnectionFactory(configurationFactory.getConfiguration());
        Consumer consumer = new RabbitMqConsumer(connectionFactory);

        consumer.bindMessageListener(new RabbitMqQueue("x.y.z"), message -> {
            System.out.println(new String(message.getBytes()));
        });

        consumer.listen();
        new Scanner(System.in).nextLine();
    }
}
```

---
### 欢迎关注，谢谢！
