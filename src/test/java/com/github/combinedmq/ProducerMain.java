package com.github.combinedmq;

import com.github.combinedmq.activemq.ActiveMqConfigurationFactory;
import com.github.combinedmq.activemq.ActiveMqConnectionFactory;
import com.github.combinedmq.activemq.ActiveMqMessage;
import com.github.combinedmq.activemq.ActiveMqProducer;
import com.github.combinedmq.activemq.ActiveMqQueue;
import com.github.combinedmq.kafka.KafkaConfigurationFactory;
import com.github.combinedmq.kafka.KafkaMessage;
import com.github.combinedmq.kafka.KafkaProducer;
import com.github.combinedmq.kafka.KafkaQueue;
import com.github.combinedmq.message.QueueType;
import com.github.combinedmq.producer.Producer;
import com.github.combinedmq.rabbitmq.RabbitMqConfigurationFactory;
import com.github.combinedmq.rabbitmq.RabbitMqConnectionFactory;
import com.github.combinedmq.rabbitmq.RabbitMqMessage;
import com.github.combinedmq.rabbitmq.RabbitMqProducer;
import com.github.combinedmq.rabbitmq.RabbitMqQueue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author xiaoyu
 */
@Slf4j
public class ProducerMain {
    @Test
    public void activemq() throws Exception {
        Producer producer = new ActiveMqProducer(new ActiveMqConnectionFactory(new ActiveMqConfigurationFactory().getConfiguration(), false));
        producer.send(new ActiveMqQueue("x.y.z", QueueType.POINT_TO_POINT), new ActiveMqMessage("这是一条activemq测试消息".getBytes()));
        producer.send(new ActiveMqQueue("x.y.z", QueueType.POINT_TO_POINT), new ActiveMqMessage("这是一条activemq测试延时消息".getBytes(), 1000L));
    }

    @Test
    public void rabbitmq() throws Exception {
        Producer producer = new RabbitMqProducer(new RabbitMqConnectionFactory(new RabbitMqConfigurationFactory().getConfiguration(), false));
        producer.send(new RabbitMqQueue("x.y.z"), new RabbitMqMessage("这是一条rabbitmq测试消息".getBytes()));
        producer.send(new RabbitMqQueue("x.y.z"), new RabbitMqMessage("这是一条rabbitmq测试延时消息".getBytes(), 1000L));
    }

    @Test
    public void kafka() throws Exception {
        Producer producer = new KafkaProducer(new KafkaConfigurationFactory().getConfiguration());
        producer.send(new KafkaQueue("x.y.z", QueueType.POINT_TO_POINT), new KafkaMessage("这是一条kafka测试消息".getBytes()));
    }

}
