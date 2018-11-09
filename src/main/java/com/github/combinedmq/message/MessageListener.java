package com.github.combinedmq.message;

/**
 * MessageListener
 *
 * @author xiaoyu
 */
public interface MessageListener {
    /**
     * onMessage
     *
     * @param message
     */
    void onMessage(Message message);
}
