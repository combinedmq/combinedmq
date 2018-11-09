package com.github.combinedmq.connection;

/**
 * @author xiaoyu
 */
public interface Connection<T> {
    T getTargetConnection();
}
