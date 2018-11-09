package com.github.combinedmq.rabbitmq;

import com.github.combinedmq.configuration.Configuration;
import lombok.Data;

/**
 * @author xiaoyu
 */

@Data
public class RabbitMqConfiguration implements Configuration {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String virtualHost;
    private ProducerPool producerPool;
    private ConsumerListener consumerListener;

    @Data
    public static class ProducerPool {
        private Integer maxTotal;
        private Integer maxIdle;
        private Integer minIdle;
        private Integer maxWaitMillis;
        private Integer minEvictableIdleTimeMillis;
        private Integer timeBetweenEvictionRunsMillis;
        private Boolean testOnBorrow;
        private Boolean testOnReturn;
        private Boolean testWhileIdle;
    }

    @Data
    public static class ConsumerListener {
        private Integer concurrency = 1;
        private Integer prefetchCount = 1;
    }
}
