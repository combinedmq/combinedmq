package com.github.combinedmq.rabbitmq;

import com.github.combinedmq.message.Queue;
import com.github.combinedmq.message.QueueType;

/**
 * @author xiaoyu
 */
public class RabbitMqQueue implements Queue {
    private static final String DEFAULT_EXCHANGE_NAME = "combinedmq.default";
    private String messageRoutingKey;
    private String exchangeName;
    private String queueBindingKey;
    private String queueName;

    public RabbitMqQueue(String queueName) {
        this(queueName, queueName, queueName);
    }

    public RabbitMqQueue(String queueName, String messageRoutingKey, String queueBindingKey) {
        this(queueName, messageRoutingKey, queueBindingKey, DEFAULT_EXCHANGE_NAME);
    }

    public RabbitMqQueue(String queueName, String messageRoutingKey, String queueBindingKey, String exchangeName) {
        this.queueName = queueName;
        this.messageRoutingKey = messageRoutingKey;
        this.queueBindingKey = queueBindingKey;
        this.exchangeName = exchangeName;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public QueueType getType() {
        throw new UnsupportedOperationException();
    }

    public String getMessageRoutingKey() {
        return messageRoutingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueBindingKey() {
        return queueBindingKey;
    }

    @Override
    public int hashCode() {
        int h;
        return ((h = messageRoutingKey.hashCode()) ^ (h >> 16)) ^
                ((h = exchangeName.hashCode()) ^ (h >> 16)) ^
                ((h = queueBindingKey.hashCode()) ^ (h >> 16)) ^
                ((h = queueName.hashCode()) ^ (h >> 16));
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        RabbitMqQueue n = (RabbitMqQueue) obj;
        return messageRoutingKey.equals(n.messageRoutingKey) &&
                exchangeName.equals(n.exchangeName) &&
                queueBindingKey.equals(n.queueBindingKey) &&
                queueName.equals(n.queueName);
    }

}
