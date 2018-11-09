package com.github.combinedmq.rabbitmq;

import com.github.combinedmq.connection.ConnectionFactory;
import com.github.combinedmq.exception.MqException;
import com.github.combinedmq.message.Message;
import com.github.combinedmq.message.Queue;
import com.github.combinedmq.producer.AbstractProducer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaoyu
 */
@Slf4j
public class RabbitMqProducer extends AbstractProducer {
    private final String exchangeType = "topic";
    private static final ConfirmListener CONFIRM_LISTENER = new DefaultConfirmListener();

    public RabbitMqProducer(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    protected void doSend(Queue queue, Message msg) throws MqException {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = (Connection) getConnectionFactory().getConnection().getTargetConnection();
            channel = connection.createChannel();
            RabbitMqQueue rabbitMqQueue = (RabbitMqQueue) queue;
            //初始化交换器和队列
            AMQP.BasicProperties.Builder builder = MessageProperties.MINIMAL_PERSISTENT_BASIC.builder();
            String messageRoutingKey = rabbitMqQueue.getMessageRoutingKey();
            if (!isQueueInitialized(queue)) {
                channel.exchangeDeclare(rabbitMqQueue.getExchangeName(), exchangeType, true);
                channel.queueDeclare(rabbitMqQueue.getQueueName(), true, false, false, null);
                channel.queueBind(rabbitMqQueue.getQueueName(), rabbitMqQueue.getExchangeName(), rabbitMqQueue.getQueueBindingKey(), null);
            }
            //通过死信模拟延迟消息功能
            if (null != msg.getDelayMillis() && msg.getDelayMillis() > 0) {
                log.info("发送延迟消息 delayMillis：{}ms", msg.getDelayMillis());
                Map<String, Object> map = new HashMap<>();
                map.put("x-dead-letter-exchange", rabbitMqQueue.getExchangeName());
                map.put("x-dead-letter-routing-key", messageRoutingKey);
                messageRoutingKey = messageRoutingKey + ".DLQ";
                channel.queueDeclare(messageRoutingKey, true, false, false, map);
                channel.queueBind(messageRoutingKey, rabbitMqQueue.getExchangeName(), messageRoutingKey, null);
                builder.expiration(String.valueOf(msg.getDelayMillis()));
            }
            //发送
            channel.confirmSelect();
            channel.basicPublish(rabbitMqQueue.getExchangeName(), messageRoutingKey, builder.build(), msg.getBytes());
            channel.addConfirmListener(CONFIRM_LISTENER);

        } catch (Exception e) {
            throw new MqException(e);
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (Exception e) {
                throw new MqException(e);
            }
        }
    }

    @Slf4j
    static class DefaultConfirmListener implements ConfirmListener {
        @Override
        public void handleAck(long deliveryTag, boolean multiple) throws IOException {
        }

        @Override
        public void handleNack(long deliveryTag, boolean multiple) throws IOException {
            log.error("confirm nack：{} {}", deliveryTag, multiple);
        }
    }
}
