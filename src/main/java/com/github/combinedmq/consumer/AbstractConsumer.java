package com.github.combinedmq.consumer;

import com.github.combinedmq.activemq.ActiveMqConfiguration;
import com.github.combinedmq.configuration.Configuration;
import com.github.combinedmq.connection.ConnectionFactory;
import com.github.combinedmq.kafka.KafkaConfiguration;
import com.github.combinedmq.message.MessageListener;
import com.github.combinedmq.message.Queue;
import com.github.combinedmq.rabbitmq.RabbitMqConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaoyu
 */
public abstract class AbstractConsumer implements Consumer {
    private ConnectionFactory connectionFactory;
    private ConcurrentHashMap<Queue, List<MessageListener>> queueListener = new ConcurrentHashMap<Queue, List<MessageListener>>();

    public AbstractConsumer() {

    }

    public AbstractConsumer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    protected Configuration getConfiguration() {
        return connectionFactory.getConfiguration();
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Consumer bindMessageListener(Queue queue, MessageListener listener) {
        Configuration cfg = getConfiguration();
        if (cfg instanceof ActiveMqConfiguration) {
            ActiveMqConfiguration configuration = (ActiveMqConfiguration) cfg;
            return this.bindMessageListener(queue, configuration.getConsumerListener().getConcurrency(), listener);
        }
        if (cfg instanceof RabbitMqConfiguration) {
            RabbitMqConfiguration configuration = (RabbitMqConfiguration) cfg;
            return this.bindMessageListener(queue, configuration.getConsumerListener().getConcurrency(), listener);
        }
        if (cfg instanceof KafkaConfiguration) {
            KafkaConfiguration configuration = (KafkaConfiguration) cfg;
            return this.bindMessageListener(queue, configuration.getConsumerListener().getConcurrency(), listener);
        }
        throw new IllegalStateException("The configuration is wrong");
    }

    @Override
    public Consumer bindMessageListener(Queue queue, Integer consumerConcurrency, MessageListener listener) {
        if (queueListener.containsKey(queue)) {
            throw new RuntimeException("Please do not repeat the binding messageListener");
        }
        List<MessageListener> listeners = new ArrayList<>();
        for (; consumerConcurrency-- > 0; ) {
            listeners.add(listener);
        }
        if (listeners.size() > 0) {
            queueListener.put(queue, listeners);
        }
        return this;
    }

    protected ConcurrentHashMap<Queue, List<MessageListener>> getQueueListener() {
        return queueListener;
    }
}
