package com.github.combinedmq.message;

/**
 * BaseMessage
 *
 * @author xiaoyu
 */
public class BaseMessage implements Message {
    private byte[] bytes;
    private Long delayMillis;


    public BaseMessage(byte[] bytes) {
        this(bytes, null);
    }

    /**
     * @param bytes       Message content bytes
     * @param delayMillis Number of milliseconds message delay,only valid for producer
     */
    public BaseMessage(byte[] bytes, Long delayMillis) {
        this.bytes = bytes;
        this.delayMillis = delayMillis;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public Long getDelayMillis() {
        return delayMillis;
    }


}
