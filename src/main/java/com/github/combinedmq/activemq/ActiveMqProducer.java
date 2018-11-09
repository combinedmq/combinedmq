package com.github.combinedmq.activemq;

import com.github.combinedmq.connection.ConnectionFactory;
import com.github.combinedmq.exception.MqException;
import com.github.combinedmq.message.Message;
import com.github.combinedmq.message.Queue;
import com.github.combinedmq.producer.AbstractProducer;
import org.apache.activemq.ScheduledMessage;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

/**
 * @author xiaoyu
 */
public class ActiveMqProducer extends AbstractProducer {

    public ActiveMqProducer(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    protected void doSend(Queue queue, Message msg) throws MqException {
        Connection connection = null;
        Session session = null;
        try {
            connection = (Connection) getConnectionFactory().getConnection().getTargetConnection();
            connection.start();
            session = connection.createSession(Boolean.TRUE, Session.SESSION_TRANSACTED);
            ActiveMqQueue activeMqQueue = (ActiveMqQueue) queue;
            Destination dest = null;
            switch (activeMqQueue.getType()) {
                case POINT_TO_POINT:
                    dest = session.createQueue(queue.getQueueName());
                    break;
                case PUBLISH_SUBSCRIBE:
                    dest = session.createTopic(queue.getQueueName());
                    break;
            }
            BytesMessage message = session.createBytesMessage();
            if (null != msg.getDelayMillis())
                message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, msg.getDelayMillis());
            message.writeBytes(msg.getBytes());
            MessageProducer producer = session.createProducer(dest);
            producer.send(message);
            session.commit();
        } catch (Exception e) {
            if (null != session) {
                try {
                    session.rollback();
                } catch (JMSException e1) {
                }
            }
            throw new MqException(e);
        } finally {
            try {
                if (null != session) {
                    session.close();
                }
                if (null != connection) {
                    connection.close();
                }
            } catch (Exception e) {
                throw new MqException(e);
            }
        }
    }
}
