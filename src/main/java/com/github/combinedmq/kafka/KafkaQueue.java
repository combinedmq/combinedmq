package com.github.combinedmq.kafka;

import com.github.combinedmq.message.Queue;
import com.github.combinedmq.message.QueueType;

/**
 * @author xiaoyu
 */
public class KafkaQueue implements Queue {
    private QueueType type;
    private String queueName;

    public KafkaQueue(String queueName, QueueType type) {
        this.queueName = queueName;
        this.type = type;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public QueueType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        int h;
        return (h = queueName.hashCode()) ^ (h >> 16) ^ type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        KafkaQueue n = (KafkaQueue) obj;
        return type.equals(n.getType()) && queueName.equals(n.queueName);
    }

}
