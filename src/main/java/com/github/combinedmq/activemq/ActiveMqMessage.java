package com.github.combinedmq.activemq;

import com.github.combinedmq.message.BaseMessage;

/**
 * @author xiaoyu
 */
public class ActiveMqMessage extends BaseMessage {

    public ActiveMqMessage(byte[] bytes) {
        super(bytes);
    }

    public ActiveMqMessage(byte[] bytes, Long delayMillis) {
        super(bytes, delayMillis);
    }
}
