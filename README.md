CombinedMq
=====================
[![Build Status](https://travis-ci.com/combinedmq/combinedmq.svg?branch=master)](https://travis-ci.com/combinedmq/combinedmq)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.combinedmq/combinedmq.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.combinedmq%22%20AND%20a:%22combinedmq%22)

[中文版文档](https://github.com/combinedmq/combinedmq/blob/master/README_zh.md)

CombinedMq is an abstract encapsulation of RabbitMq, ActiveMq, Kafka message queue component.

## Using the step
If you use the Spring framework, you can use either combinedmq-spring-boot-starter or combinedmq-spring directly.
- Maven dependency：

```xml
<dependency>
    <groupId>com.github.combinedmq</groupId>
    <artifactId>combinedmq</artifactId>
    <version>1.0.0</version>
</dependency>
```
- Configuration file -- add the combinedmq.yml file to the resource root directory. If you use rabbitmq, add the following to the yml file:
 

```yaml
rabbitmq:
  host: 10.1.7.22
  port: 5672
  username: xiaoyu
  password: xiaoyu
  virtualHost: /
  consumerListener:
    concurrency: 5 #Number of consumers
  producerPool: #Connection pool configuration, connection pool is only valid for producer
    maxTotal: 100
    maxIdle: 20
    minIdle: 10
    maxWaitMillis: 30000
    minEvictableIdleTimeMillis: 60000
    timeBetweenEvictionRunsMillis: 30000
    testOnBorrow: false
    testOnReturn: false
    testWhileIdle: true
# ConsumerListener and producerPool can not exist at the same time
```
Other configurations can view this [configuration file](https://github.com/combinedmq/combinedmq/blob/master/src/test/resources/combinedmq.yml)。

- Producer code implementation

```java
public class ProducerTest {
    public static void main(String[] args) throws MqException {
        ConfigurationFactory configurationFactory = new RabbitMqConfigurationFactory();
        ConnectionFactory connectionFactory = new RabbitMqConnectionFactory(configurationFactory.getConfiguration());

        Producer producer = new RabbitMqProducer(connectionFactory);
        producer.send(new RabbitMqQueue("x.y.z"), new RabbitMqMessage("This is a rabbitmq test message".getBytes()));
        producer.send(new RabbitMqQueue("x.y.z"), new RabbitMqMessage("This is a rabbitmq test delay message".getBytes(), 60000L));
    }
}
```

- Consumer code implementation

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
### Welcome, thanks!
