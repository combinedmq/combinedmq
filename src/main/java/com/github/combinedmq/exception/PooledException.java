package com.github.combinedmq.exception;

/**
 * @author xiaoyu
 */
public class PooledException extends RuntimeException {
    public PooledException() {
    }

    public PooledException(String message) {
        super(message);
    }

    public PooledException(String message, Throwable cause) {
        super(message, cause);
    }

    public PooledException(Throwable cause) {
        super(cause);
    }

    public PooledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
