package com.github.combinedmq.consumer;

import com.github.combinedmq.exception.MqException;
import com.github.combinedmq.message.MessageListener;
import com.github.combinedmq.message.Queue;

/**
 * @author xiaoyu
 */
public interface Consumer {
    Consumer bindMessageListener(Queue queue, MessageListener listener);

    Consumer bindMessageListener(Queue queue, Integer consumerConcurrency, MessageListener listener);

    void listen() throws MqException;

}
