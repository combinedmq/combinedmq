package com.github.combinedmq.connection;

import com.github.combinedmq.configuration.Configuration;

/**
 * @author xiaoyu
 */
public abstract class AbstractConnectionFactory implements ConnectionFactory {
    private Configuration configuration;

    public AbstractConnectionFactory(Configuration configuration) {
        this(configuration, true);
    }

    public AbstractConnectionFactory(Configuration configuration, boolean isUsePool) {
        this.configuration = configuration;
        init();
        if (isUsePool) {
            initPool();
        }
    }

    protected abstract void init();

    protected abstract void initPool();

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
