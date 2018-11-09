package com.github.combinedmq.producer;

import com.github.combinedmq.connection.ConnectionFactory;
import com.github.combinedmq.exception.MqException;
import com.github.combinedmq.message.Message;
import com.github.combinedmq.message.Queue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xiaoyu
 */
public abstract class AbstractProducer implements Producer {
    private ConnectionFactory connectionFactory;

    private ConcurrentMap<Queue, Boolean> queues = new ConcurrentHashMap<>();


    public AbstractProducer() {

    }

    public AbstractProducer(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    protected boolean isQueueInitialized(Queue queue) {
        return queues.containsKey(queue);
    }

    @Override
    public void send(Queue queue, Message msg) throws MqException {
        doSend(queue, msg);
        queues.putIfAbsent(queue, Boolean.TRUE);
    }

    protected abstract void doSend(Queue queue, Message msg) throws MqException;
}
