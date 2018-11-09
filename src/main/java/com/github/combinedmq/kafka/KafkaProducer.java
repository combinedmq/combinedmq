package com.github.combinedmq.kafka;

import com.github.combinedmq.configuration.Configuration;
import com.github.combinedmq.exception.MqException;
import com.github.combinedmq.message.Message;
import com.github.combinedmq.message.Queue;
import com.github.combinedmq.pool.kafka.KafkaPoolConfig;
import com.github.combinedmq.pool.kafka.KafkaProducerPool;
import com.github.combinedmq.producer.AbstractProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.Properties;

/**
 * @author xiaoyu
 */
@Slf4j
public class KafkaProducer extends AbstractProducer {
    private KafkaConfiguration configuration;
    private KafkaProducerPool pool;

    public KafkaProducer(Configuration configuration) {
        this.configuration = (KafkaConfiguration) configuration;
        initPool(configuration);
    }

    private void initPool(Configuration configuration) {
        KafkaConfiguration cfg = (KafkaConfiguration) configuration;
        if (null == cfg.getProducerPool()) {
            throw new NullPointerException("The pool configuration does not exist");
        }
        KafkaPoolConfig poolConfig = new KafkaPoolConfig();
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

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, cfg.getBootstrapServers());
//        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        if ((value = cfg.getProducerPool().getBatchSize()) != null) props.put(ProducerConfig.BATCH_SIZE_CONFIG, value);
        if ((value = cfg.getProducerPool().getLingerMillis()) != null)
            props.put(ProducerConfig.LINGER_MS_CONFIG, value);
        pool = new KafkaProducerPool(poolConfig, props);
    }

    @Override
    protected void doSend(Queue queue, Message msg) throws MqException {
        Producer producer = null;
        try {
            producer = pool.getResource();
            //发送
            KafkaQueue kafkaQueue = (KafkaQueue) queue;
            producer.send(new ProducerRecord(kafkaQueue.getQueueName(), msg.getBytes()));
        } catch (Exception e) {
            throw new MqException(e);
        } finally {
            try {
                if (null != producer) {
                    producer.close();
                }
            } catch (Exception e) {
                throw new MqException(e);
            }
        }
    }
}
