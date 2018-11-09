package com.github.combinedmq.rabbitmq;


import com.github.combinedmq.configuration.Configuration;
import com.github.combinedmq.connection.ConnectionFactory;
import com.github.combinedmq.consumer.AbstractConsumer;
import com.github.combinedmq.exception.MqException;
import com.github.combinedmq.message.MessageExecutor;
import com.github.combinedmq.message.MessageListener;
import com.github.combinedmq.message.Queue;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author xiaoyu
 */
@Slf4j
public class RabbitMqConsumer extends AbstractConsumer {
    private final String exchangeType = "topic";
    private final boolean autoAck = false;

    private Connection connection;

    public RabbitMqConsumer(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public void listen() throws MqException {
        try {
            if (null == connection) {
                connection = (Connection) getConnectionFactory().getConnection().getTargetConnection();
            }
            ConcurrentHashMap<Queue, List<MessageListener>> queueListener = getQueueListener();
            Set<Map.Entry<Queue, List<MessageListener>>> entries = queueListener.entrySet();
            for (Map.Entry<Queue, List<MessageListener>> entry : entries) {
                Queue q = entry.getKey();
                RabbitMqQueue rabbitMqQueue = (RabbitMqQueue) q;
                List<MessageListener> mls = entry.getValue();
                MessageExecutor executor = new MessageExecutor("rabbitmq", mls.size());
                for (MessageListener ml : mls) {
                    if (null == executor.getMessageListener()) {
                        executor.setMessageListener(ml);
                    }
                    Channel channel = connection.createChannel();
                    Integer prefetchCount = null;
                    Configuration config = getConfiguration();
                    if (config instanceof RabbitMqConfiguration) {
                        prefetchCount = ((RabbitMqConfiguration) config).getConsumerListener().getPrefetchCount();
                    }
                    channel.basicQos(prefetchCount != null ? prefetchCount : 1);
                    //初始化交换器和队列
                    channel.exchangeDeclare(rabbitMqQueue.getExchangeName(), exchangeType, true);
                    channel.queueDeclare(rabbitMqQueue.getQueueName(), true, false, false, null);
                    channel.queueBind(rabbitMqQueue.getQueueName(), rabbitMqQueue.getExchangeName(), rabbitMqQueue.getQueueBindingKey(), null);

                    channel.basicConsume(rabbitMqQueue.getQueueName(), autoAck, new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            Future<?> future = executor.onMessage(new RabbitMqMessage(body));
                            try {
                                future.get();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            } finally {
                                getChannel().basicAck(envelope.getDeliveryTag(), false);
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            throw new MqException(e);
        }
    }

}
