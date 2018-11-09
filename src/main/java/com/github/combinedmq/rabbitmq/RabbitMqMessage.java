package com.github.combinedmq.rabbitmq;

import com.github.combinedmq.message.BaseMessage;

/**
 * @author xiaoyu
 */
public class RabbitMqMessage extends BaseMessage {


    public RabbitMqMessage(byte[] bytes) {
        super(bytes);
    }

    public RabbitMqMessage(byte[] bytes, Long delayMillis) {
        super(bytes, delayMillis);
    }
}
