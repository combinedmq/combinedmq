package com.github.combinedmq.connection;

import com.github.combinedmq.configuration.Configuration;
import com.github.combinedmq.exception.ConnectionException;

/**
 * @author xiaoyu
 */
public interface ConnectionFactory {
    Connection getConnection() throws ConnectionException;

    Configuration getConfiguration();

}
