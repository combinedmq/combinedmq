package com.github.combinedmq.pool.kafka;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class KafkaPoolConfig extends GenericObjectPoolConfig {
    public KafkaPoolConfig() {
        setTestWhileIdle(true);
        setMinEvictableIdleTimeMillis(60000);
        setTimeBetweenEvictionRunsMillis(30000);
        setNumTestsPerEvictionRun(-1);
    }
}
