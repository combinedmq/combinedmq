package com.github.combinedmq;

import com.github.combinedmq.configuration.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * AbstractConfigurationFactory
 *
 * @author xiaoyu
 * @see ConfigurationFactory
 * @see com.github.combinedmq.rabbitmq.RabbitMqConfigurationFactory
 * @see com.github.combinedmq.activemq.ActiveMqConfigurationFactory
 * @see com.github.combinedmq.kafka.KafkaConfigurationFactory
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractConfigurationFactory implements ConfigurationFactory {
    public static String CONFIG_FILE = "combinedmq.yml";

    /**
     * Config prefix
     *
     * @return
     */
    protected abstract String getConfigPrefix();

    protected abstract Class<? extends Configuration> getConfigurationClass();

    private volatile Configuration configuration;

    private Yaml yaml = new Yaml();

    private void init() {
        synchronized (this) {
            if (this.configuration != null) {
                return;
            }
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
            if (null == is) {
//                is = AbstractConfigurationFactory.class.getResourceAsStream(CONFIG_FILE);
            }
            if (null == is) {
                throw new NullPointerException("The configuration file " + CONFIG_FILE + " does not exist");
            }
            Object o = ((Map) yaml.load(is)).get(getConfigPrefix());
            this.configuration = yaml.loadAs(yaml.dump(o), getConfigurationClass());
        }
    }

    @Override
    public Configuration getConfiguration() {
        init();
        return this.configuration;
    }
}
