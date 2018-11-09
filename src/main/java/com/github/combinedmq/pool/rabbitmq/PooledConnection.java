package com.github.combinedmq.pool.rabbitmq;

import com.github.combinedmq.exception.PooledException;
import com.rabbitmq.client.BlockedCallback;
import com.rabbitmq.client.BlockedListener;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ExceptionHandler;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.UnblockedCallback;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaoyu
 */
public class PooledConnection implements Connection {
    private Connection connection;
    private Channel channel;
    private GenericObjectPool<PooledConnection> connectionsPool;

    public PooledConnection(Connection connection) {
        this.connection = connection;
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            throw new PooledException(e);
        }
    }

    public void disconnect() throws IOException {
        try {
            channel.close();
        } catch (TimeoutException e) {
            throw new IOException(e);
        } finally {
            connection.close();
        }
    }

    public void setConnectionsPool(GenericObjectPool<PooledConnection> connectionsPool) {
        this.connectionsPool = connectionsPool;
    }

    @Override
    public InetAddress getAddress() {
        return connection.getAddress();
    }

    @Override
    public int getPort() {
        return connection.getPort();
    }

    @Override
    public int getChannelMax() {
        return connection.getChannelMax();
    }

    @Override
    public int getFrameMax() {
        return connection.getFrameMax();
    }

    @Override
    public int getHeartbeat() {
        return connection.getHeartbeat();
    }

    @Override
    public Map<String, Object> getClientProperties() {
        return connection.getClientProperties();
    }

    @Override
    public String getClientProvidedName() {
        return connection.getClientProvidedName();
    }

    @Override
    public Map<String, Object> getServerProperties() {
        return connection.getServerProperties();
    }

    @Override
    public Channel createChannel() throws IOException {
        return channel;
    }

    @Override
    public Channel createChannel(int channelNumber) throws IOException {
        return connection.createChannel(channelNumber);
    }

    @Override
    public void close() throws IOException {
        connectionsPool.returnObject(this);
    }

    @Override
    @Deprecated
    public void close(int closeCode, String closeMessage) throws IOException {
        this.close();
    }

    @Override
    @Deprecated
    public void close(int timeout) throws IOException {
        this.close();
    }

    @Override
    @Deprecated
    public void close(int closeCode, String closeMessage, int timeout) throws IOException {
        this.close();
    }

    @Override
    public void abort() {
        connection.abort();
    }

    @Override
    public void abort(int closeCode, String closeMessage) {
        connection.abort(closeCode, closeMessage);
    }

    @Override
    public void abort(int timeout) {
        connection.abort(timeout);
    }

    @Override
    public void abort(int closeCode, String closeMessage, int timeout) {
        connection.abort(closeCode, closeMessage, timeout);
    }

    @Override
    public void addBlockedListener(BlockedListener listener) {
        connection.addBlockedListener(listener);
    }

    @Override
    public BlockedListener addBlockedListener(BlockedCallback blockedCallback, UnblockedCallback unblockedCallback) {
        return connection.addBlockedListener(blockedCallback, unblockedCallback);
    }

    @Override
    public boolean removeBlockedListener(BlockedListener listener) {
        return connection.removeBlockedListener(listener);
    }

    @Override
    public void clearBlockedListeners() {
        connection.clearBlockedListeners();
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return connection.getExceptionHandler();
    }

    @Override
    public String getId() {
        return connection.getId();
    }

    @Override
    public void setId(String id) {
        connection.setId(id);
    }

    @Override
    public void addShutdownListener(ShutdownListener listener) {
        connection.addShutdownListener(listener);
    }

    @Override
    public void removeShutdownListener(ShutdownListener listener) {
        connection.removeShutdownListener(listener);
    }

    @Override
    public ShutdownSignalException getCloseReason() {
        return connection.getCloseReason();
    }

    @Override
    public void notifyListeners() {
        connection.notifyListeners();
    }

    @Override
    public boolean isOpen() {
        return connection.isOpen();
    }

}
