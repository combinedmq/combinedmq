package com.github.combinedmq.kafka;

import com.github.combinedmq.configuration.Configuration;
import lombok.Data;

/**
 * @author xiaoyu
 */
@Data
public class KafkaConfiguration implements Configuration {
    private String bootstrapServers;
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
        private Integer batchSize;
        private Integer lingerMillis;
    }

    @Data
    public static class ConsumerListener {
        private Integer concurrency = 1;
        private Integer maxPollRecord = 1;
    }

}
