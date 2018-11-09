package com.github.combinedmq.pool.kafka;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoyu
 */
public class PooledKafkaProducer implements Producer<byte[], byte[]> {

    private Producer<byte[], byte[]> producer;
    private GenericObjectPool<PooledKafkaProducer> objectPool;

    public PooledKafkaProducer(KafkaProducer<byte[], byte[]> kafkaProducer) {
        this.producer = kafkaProducer;
    }

    public void setObjectPool(GenericObjectPool<PooledKafkaProducer> objectPool) {
        this.objectPool = objectPool;
    }

    @Override
    public void initTransactions() {
        producer.initTransactions();
    }

    @Override
    public void beginTransaction() throws ProducerFencedException {
        producer.beginTransaction();
    }

    @Override
    public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets, String consumerGroupId) throws ProducerFencedException {
        producer.sendOffsetsToTransaction(offsets, consumerGroupId);
    }

    @Override
    public void commitTransaction() throws ProducerFencedException {
        producer.commitTransaction();
    }

    @Override
    public void abortTransaction() throws ProducerFencedException {
        producer.abortTransaction();
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord<byte[], byte[]> record) {
        return producer.send(record);
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord<byte[], byte[]> record, Callback callback) {
        return producer.send(record, callback);
    }

    @Override
    public void flush() {
        producer.flush();
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic) {
        return producer.partitionsFor(topic);
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return producer.metrics();
    }

    @Override
    public void close() {
        objectPool.returnObject(this);
    }

    void disconnect() {
        producer.close();
    }

    @Override
    public void close(long timeout, TimeUnit timeUnit) {
        producer.close(timeout, timeUnit);
    }
}
