package com.github.combinedmq.kafka;

import com.github.combinedmq.message.BaseMessage;

/**
 * @author xiaoyu
 */
public class KafkaMessage extends BaseMessage {

    public KafkaMessage(byte[] bytes) {
        super(bytes);
    }

    /**
     * Kafka does not support delayed messages
     *
     * @return
     */
    @Deprecated
    @Override
    public Long getDelayMillis() {
        throw new UnsupportedOperationException();
    }
}
