package com.github.combinedmq.activemq;

import com.github.combinedmq.connection.ConnectionFactory;
import com.github.combinedmq.consumer.AbstractConsumer;
import com.github.combinedmq.exception.MqException;
import com.github.combinedmq.message.MessageExecutor;
import com.github.combinedmq.message.MessageListener;
import com.github.combinedmq.message.Queue;
import com.github.combinedmq.util.UnsafeUtils;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.util.ByteSequence;
import sun.misc.Unsafe;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author xiaoyu
 */
public class ActiveMqConsumer extends AbstractConsumer {
    private Connection connection;
    private Unsafe unsafe = UnsafeUtils.getUnsafe();

    public ActiveMqConsumer(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public void listen() throws MqException {
        try {
            if (null == connection) {
                connection = (Connection) getConnectionFactory().getConnection().getTargetConnection();
                connection.start();
            }
            ConcurrentHashMap<Queue, List<MessageListener>> queueListener = getQueueListener();
            Set<Map.Entry<Queue, List<MessageListener>>> entries = queueListener.entrySet();
            for (Map.Entry<Queue, List<MessageListener>> entry : entries) {
                Queue q = entry.getKey();
                ActiveMqQueue activeMqQueue = (ActiveMqQueue) q;
                List<MessageListener> mls = entry.getValue();
                MessageExecutor executor = new MessageExecutor("activemq", mls.size());
                for (final MessageListener ml : mls) {
                    if (null == executor.getMessageListener()) {
                        executor.setMessageListener(ml);
                    }
                    Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
                    Destination dest = null;
                    switch (activeMqQueue.getType()) {
                        case POINT_TO_POINT:
                            dest = session.createQueue(q.getQueueName());
                            break;
                        case PUBLISH_SUBSCRIBE:
                            dest = session.createTopic(q.getQueueName());
                            break;
                    }
                    MessageConsumer consumer = session.createConsumer(dest);
                    consumer.setMessageListener(message -> {
                        ActiveMQBytesMessage msg = (ActiveMQBytesMessage) message;
                        try {
                            ByteSequence dataIn = (ByteSequence) unsafe.getObject(msg, unsafe.objectFieldOffset(org.apache.activemq.command.Message.class.getDeclaredField("content")));
                            Future<?> future = executor.onMessage(new ActiveMqMessage(dataIn.getData()));
                            try {
                                future.get();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            } finally {
                                msg.acknowledge();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        } catch (Exception e) {
            throw new MqException(e);
        }
    }

}
