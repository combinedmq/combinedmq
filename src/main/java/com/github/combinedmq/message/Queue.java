package com.github.combinedmq.message;

/**
 * Queue
 *
 * @author xiaoyu
 * @since 1.0.0
 */
public interface Queue {
    /**
     * getQueueName
     *
     * @return queueName
     */
    String getQueueName();

    /**
     * getType
     *
     * @return queueType
     */
    QueueType getType();

}
