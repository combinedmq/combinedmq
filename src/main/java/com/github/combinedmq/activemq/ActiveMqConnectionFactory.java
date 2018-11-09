package com.github.combinedmq.activemq;

import com.github.combinedmq.configuration.Configuration;
import com.github.combinedmq.connection.AbstractConnectionFactory;
import com.github.combinedmq.connection.Connection;
import com.github.combinedmq.exception.ConnectionException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.JMSException;

/**
 * @author xiaoyu
 */
public class ActiveMqConnectionFactory extends AbstractConnectionFactory {
    private PooledConnectionFactory pooledCf;
    private ActiveMQConnectionFactory cf;
    private ActiveMqConfiguration cfg;

    public ActiveMqConnectionFactory(Configuration configuration) {
        super(configuration);
    }

    public ActiveMqConnectionFactory(Configuration configuration, boolean isUsePool) {
        super(configuration, isUsePool);
    }

    @Override
    protected void init() {
        cfg = (ActiveMqConfiguration) getConfiguration();
        if (null == cf) {
            cf = new ActiveMQConnectionFactory();
        }
        Object value;
        if ((value = cfg.getBrokerUrl()) != null) cf.setBrokerURL(String.valueOf(value));
        if ((value = cfg.getUsername()) != null) cf.setUserName(String.valueOf(value));
        if ((value = cfg.getPassword()) != null) cf.setPassword(String.valueOf(value));
        ActiveMQPrefetchPolicy policy = new ActiveMQPrefetchPolicy();
        policy.setQueuePrefetch(1);
        cf.setPrefetchPolicy(policy);
    }

    @Override
    protected void initPool() {
        if (null == pooledCf && null != cfg.getProducerPool()) {
            pooledCf = new PooledConnectionFactory(cf);
            Object value;
            if ((value = cfg.getProducerPool().getMaxConnections()) != null) {
                pooledCf.setMaxConnections((Integer) value);
            }
            if ((value = cfg.getProducerPool().getIdleTimeout()) != null) pooledCf.setIdleTimeout((Integer) value);
            if ((value = cfg.getProducerPool().getExpiryTimeout()) != null) pooledCf.setExpiryTimeout((Integer) value);
            if ((value = cfg.getProducerPool().getTimeBetweenExpirationCheckMillis()) != null)
                pooledCf.setTimeBetweenExpirationCheckMillis((Integer) value);
        }
    }


    @Override
    public Connection getConnection() throws ConnectionException {
        if (pooledCf != null) {
            try {
                return new ActiveMqConnection(pooledCf.createConnection());
            } catch (JMSException e) {
                throw new ConnectionException(e);
            }
        }
        try {
            return new ActiveMqConnection(cf.createConnection());
        } catch (JMSException e) {
            throw new ConnectionException(e);
        }
    }


}
