package com.github.combinedmq.rabbitmq;

import com.github.combinedmq.AbstractConfigurationFactory;
import com.github.combinedmq.configuration.Configuration;

/**
 * @author xiaoyu
 */
public class RabbitMqConfigurationFactory extends AbstractConfigurationFactory {
    private static final String CONFIG_PREFIX = "rabbitmq";
    private static final Class<? extends Configuration> CONFIG_CLASS = RabbitMqConfiguration.class;

    @Override
    protected String getConfigPrefix() {
        return CONFIG_PREFIX;
    }

    @Override
    protected Class<? extends Configuration> getConfigurationClass() {
        return CONFIG_CLASS;
    }
}
