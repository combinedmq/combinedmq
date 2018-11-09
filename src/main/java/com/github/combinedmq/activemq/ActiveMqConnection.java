package com.github.combinedmq.activemq;

import javax.jms.Connection;

/**
 * @author xiaoyu
 */
public class ActiveMqConnection implements com.github.combinedmq.connection.Connection<Connection> {
    private javax.jms.Connection connection;

    public ActiveMqConnection(javax.jms.Connection connection) {
        this.connection = connection;
    }

    @Override
    public javax.jms.Connection getTargetConnection() {
        return connection;
    }
}
