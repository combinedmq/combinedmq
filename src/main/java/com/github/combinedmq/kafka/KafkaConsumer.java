package com.github.combinedmq.kafka;


import com.github.combinedmq.configuration.Configuration;
import com.github.combinedmq.consumer.AbstractConsumer;
import com.github.combinedmq.exception.MqException;
import com.github.combinedmq.message.MessageExecutor;
import com.github.combinedmq.message.MessageListener;
import com.github.combinedmq.message.Queue;
import com.github.combinedmq.message.QueueType;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author xiaoyu
 */
@Slf4j
public class KafkaConsumer extends AbstractConsumer {
    private Properties props;
    private Configuration configuration;

    public KafkaConsumer(Configuration configuration) {
        this.configuration = configuration;
        init(configuration);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    private void init(Configuration configuration) {
        KafkaConfiguration cfg = (KafkaConfiguration) configuration;
        if (null == cfg.getConsumerListener()) {
            throw new NullPointerException("The consumer configuration does not exist");
        }
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, cfg.getBootstrapServers());

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, cfg.getConsumerListener().getMaxPollRecord());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

        this.props = props;
    }

    @Override
    public void listen() throws MqException {
        try {
            ConcurrentHashMap<Queue, List<MessageListener>> queueListener = getQueueListener();
            Set<Map.Entry<Queue, List<MessageListener>>> entries = queueListener.entrySet();
            for (Map.Entry<Queue, List<MessageListener>> entry : entries) {
                Queue q = entry.getKey();
                KafkaQueue kafkaQueue = (KafkaQueue) q;
                List<MessageListener> mls = entry.getValue();
                MessageExecutor executor = new MessageExecutor("kafka", mls.size());
                for (int i = 0; i < mls.size(); i++) {
                    MessageListener ml = mls.get(i);
                    if (null == executor.getMessageListener()) {
                        executor.setMessageListener(ml);
                    }
                    QueueType type = kafkaQueue.getType();
                    if (QueueType.POINT_TO_POINT == type) {
                        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
                        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaQueue.getQueueName() + "_ptp");
                    } else if (QueueType.PUBLISH_SUBSCRIBE == type) {
                        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
                        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaQueue.getQueueName() + "_pts" + "_" + InetAddress.getLocalHost().getHostAddress() + "_" + i);
                    }
                    Consumer<byte[], byte[]> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
                    consumer.subscribe(Collections.singletonList(kafkaQueue.getQueueName()));
                    new Thread(() -> {
                        for (; ; ) {
                            ConsumerRecords<byte[], byte[]> records = consumer.poll(30000);
                            for (ConsumerRecord<byte[], byte[]> record : records) {
                                Future<?> future = executor.onMessage(new KafkaMessage(record.value()));
                                try {
                                    future.get();
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                } finally {
                                    consumer.commitSync();
                                }
                            }
                        }
                    }).start();
                }
            }
        } catch (Exception e) {
            throw new MqException(e);
        }
    }

}
