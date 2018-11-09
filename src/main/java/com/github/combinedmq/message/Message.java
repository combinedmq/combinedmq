package com.github.combinedmq.message;

/**
 * Message
 *
 * @author xiaoyu
 * @since 1.0.0
 */
public interface Message {
    /**
     * Message content bytes
     *
     * @return bytes
     */
    byte[] getBytes();

    /**
     * Number of milliseconds message delay,only valid for producer
     *
     * @return
     */
    Long getDelayMillis();
}
