package com.github.combinedmq.pool.rabbitmq;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RabbitMqPoolConfig extends GenericObjectPoolConfig {
    public RabbitMqPoolConfig() {
        setTestWhileIdle(true);
        setMinEvictableIdleTimeMillis(60000);
        setTimeBetweenEvictionRunsMillis(30000);
        setNumTestsPerEvictionRun(-1);
    }
}
