package com.github.combinedmq;

import com.github.combinedmq.activemq.ActiveMqConfigurationFactory;
import com.github.combinedmq.activemq.ActiveMqConnectionFactory;
import com.github.combinedmq.activemq.ActiveMqConsumer;
import com.github.combinedmq.activemq.ActiveMqQueue;
import com.github.combinedmq.consumer.Consumer;
import com.github.combinedmq.kafka.KafkaConfigurationFactory;
import com.github.combinedmq.kafka.KafkaConsumer;
import com.github.combinedmq.kafka.KafkaQueue;
import com.github.combinedmq.message.QueueType;
import com.github.combinedmq.rabbitmq.RabbitMqConfigurationFactory;
import com.github.combinedmq.rabbitmq.RabbitMqConnectionFactory;
import com.github.combinedmq.rabbitmq.RabbitMqConsumer;
import com.github.combinedmq.rabbitmq.RabbitMqQueue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Scanner;

/**
 * @author xiaoyu
 */
@Slf4j
public class ConsumerMain {
    @Test
    public void activemq() throws Exception {
        Consumer consumer = new ActiveMqConsumer(new ActiveMqConnectionFactory(new ActiveMqConfigurationFactory().getConfiguration(), false));
        consumer.bindMessageListener(new ActiveMqQueue("x.y.z", QueueType.POINT_TO_POINT), message -> {
            log.info(new String(message.getBytes()));
        });
        consumer.listen();
        new Scanner(System.in).nextLine();
    }

    @Test
    public void rabbitmq() throws Exception {
        Consumer consumer = new RabbitMqConsumer(new RabbitMqConnectionFactory(new RabbitMqConfigurationFactory().getConfiguration(), false));
        consumer.bindMessageListener(new RabbitMqQueue("x.y.z"), message -> {
            log.info(new String(message.getBytes()));
        });
        consumer.listen();
        new Scanner(System.in).nextLine();
    }

    @Test
    public void kafka() throws Exception {
        Consumer consumer = new KafkaConsumer(new KafkaConfigurationFactory().getConfiguration());
        consumer.bindMessageListener(new KafkaQueue("x.y.z", QueueType.POINT_TO_POINT), message -> {
            log.info(new String(message.getBytes()));
        });
        consumer.listen();
        new Scanner(System.in).nextLine();
    }

}
