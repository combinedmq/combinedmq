package com.github.combinedmq.kafka;

import com.github.combinedmq.AbstractConfigurationFactory;
import com.github.combinedmq.configuration.Configuration;

/**
 * @author xiaoyu
 */
public class KafkaConfigurationFactory extends AbstractConfigurationFactory {
    private static final String CONFIG_PREFIX = "kafka";
    private static final Class<? extends Configuration> CONFIG_CLASS = KafkaConfiguration.class;

    @Override
    protected String getConfigPrefix() {
        return CONFIG_PREFIX;
    }

    @Override
    protected Class<? extends Configuration> getConfigurationClass() {
        return CONFIG_CLASS;
    }
}
