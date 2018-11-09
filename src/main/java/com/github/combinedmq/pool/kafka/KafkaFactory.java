package com.github.combinedmq.pool.kafka;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/***
 *
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *
 */
public class KafkaFactory implements PooledObjectFactory<PooledKafkaProducer> {
    private Properties config;

    public KafkaFactory(Properties config) {
        this.config = config;
    }

    @Override
    public PooledObject<PooledKafkaProducer> makeObject() throws Exception {
        PooledKafkaProducer producer = new PooledKafkaProducer(new KafkaProducer<>(config));
        return new DefaultPooledObject<>(producer);
    }

    @Override
    public void destroyObject(PooledObject<PooledKafkaProducer> p) throws Exception {
        try {
            PooledKafkaProducer producer = p.getObject();
            producer.disconnect();
        } catch (Throwable e) {
        }
    }

    @Override
    public boolean validateObject(PooledObject<PooledKafkaProducer> p) {
        try {
            p.getObject().flush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void activateObject(PooledObject<PooledKafkaProducer> p) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<PooledKafkaProducer> p) throws Exception {

    }
}
