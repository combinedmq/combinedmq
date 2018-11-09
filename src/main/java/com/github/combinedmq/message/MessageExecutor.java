package com.github.combinedmq.message;

import com.github.combinedmq.util.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaoyu
 */
public class MessageExecutor {
    private static final AtomicInteger QUEUE_NUM = new AtomicInteger(1);
    private ExecutorService executor;
    private MessageListener messageListener;
    private ThreadFactory threadFactory;

    public MessageExecutor(String executorName, int threads) {
        threadFactory = new NamedThreadFactory(executorName + "-" + QUEUE_NUM.getAndIncrement());
        this.executor = new ThreadPoolExecutor(threads, threads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }

    public Future<?> onMessage(Message message) {
        return executor.submit(() -> messageListener.onMessage(message));
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

}
