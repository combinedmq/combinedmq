package com.github.combinedmq.activemq;

import com.github.combinedmq.message.Queue;
import com.github.combinedmq.message.QueueType;

/**
 * @author xiaoyu
 */
public class ActiveMqQueue implements Queue {
    private QueueType type;
    private String queueName;


    public ActiveMqQueue(String queueName, QueueType type) {
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
        ActiveMqQueue n = (ActiveMqQueue) obj;
        return type.equals(n.getType()) && queueName.equals(n.queueName);
    }

}
