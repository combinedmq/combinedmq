package com.github.combinedmq.rabbitmq;

import com.github.combinedmq.configuration.Configuration;
import com.github.combinedmq.connection.AbstractConnectionFactory;
import com.github.combinedmq.connection.Connection;
import com.github.combinedmq.exception.ConnectionException;
import com.github.combinedmq.pool.rabbitmq.PooledConnectionFactory;
import com.github.combinedmq.pool.rabbitmq.RabbitMqPoolConfig;


/**
 * @author xiaoyu
 */
public class RabbitMqConnectionFactory extends AbstractConnectionFactory {
    private PooledConnectionFactory pooledCf;
    private com.rabbitmq.client.ConnectionFactory cf;
    private RabbitMqConfiguration cfg;

    public RabbitMqConnectionFactory(Configuration configuration) {
        super(configuration);
    }

    public RabbitMqConnectionFactory(Configuration configuration, boolean isUsePool) {
        super(configuration, isUsePool);
    }

    @Override
    protected void init() {
        cfg = (RabbitMqConfiguration) getConfiguration();
        if (null == cf) {
            cf = new com.rabbitmq.client.ConnectionFactory();
        }
        Object value;
        if ((value = cfg.getHost()) != null) cf.setHost(String.valueOf(value));
        if ((value = cfg.getPort()) != null) cf.setPort((Integer) value);
        if ((value = cfg.getUsername()) != null) cf.setUsername(String.valueOf(value));
        if ((value = cfg.getPassword()) != null) cf.setPassword(String.valueOf(value));
        if ((value = cfg.getVirtualHost()) != null) cf.setVirtualHost(String.valueOf(value));
    }

    @Override
    protected void initPool() {
        if (null == pooledCf && null != cfg.getProducerPool()) {
            RabbitMqPoolConfig poolConfig = new RabbitMqPoolConfig();
            Object value;
            if ((value = cfg.getProducerPool().getMaxTotal()) != null) poolConfig.setMaxTotal((Integer) value);
            if ((value = cfg.getProducerPool().getMaxIdle()) != null) poolConfig.setMaxIdle((Integer) value);
            if ((value = cfg.getProducerPool().getMinIdle()) != null) poolConfig.setMinIdle((Integer) value);
            if ((value = cfg.getProducerPool().getMaxWaitMillis()) != null)
                poolConfig.setMaxWaitMillis((Integer) value);
            if ((value = cfg.getProducerPool().getMinEvictableIdleTimeMillis()) != null)
                poolConfig.setMinEvictableIdleTimeMillis((Integer) value);
            if ((value = cfg.getProducerPool().getTimeBetweenEvictionRunsMillis()) != null)
                poolConfig.setTimeBetweenEvictionRunsMillis((Integer) value);
            if ((value = cfg.getProducerPool().getTestOnBorrow()) != null) poolConfig.setTestOnBorrow((Boolean) value);
            if ((value = cfg.getProducerPool().getTestOnReturn()) != null) poolConfig.setTestOnReturn((Boolean) value);
            if ((value = cfg.getProducerPool().getTestWhileIdle()) != null)
                poolConfig.setTestWhileIdle((Boolean) value);
            pooledCf = new PooledConnectionFactory(poolConfig, cf);
        }
    }

    @Override
    public Connection getConnection() throws ConnectionException {
        if (pooledCf != null) {
            try {
                return new RabbitMqConnection(pooledCf.newConnection());
            } catch (Exception e) {
                throw new ConnectionException(e);
            }
        }
        try {
            return new RabbitMqConnection(cf.newConnection());
        } catch (Exception e) {
            throw new ConnectionException(e);
        }
    }

}
