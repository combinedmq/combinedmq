package com.github.combinedmq.producer;

import com.github.combinedmq.exception.MqException;
import com.github.combinedmq.message.Message;
import com.github.combinedmq.message.Queue;

/**
 * @author xiaoyu
 */
public interface Producer {
    void send(Queue queue, Message msg) throws MqException;


}
