package com.venusiot.vehicle.kafka;


import com.google.common.base.Preconditions;
import com.venusiot.vehicle.config.ServerConfig;
import com.venusiot.vehicle.util.CommonUtil;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 
 */
public final class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private final Producer<byte[], byte[]> producer;

    private final ProducerConfig config;

    private static final int DEFAULT_BATCH_NUM__MESSAGES =
            Integer.getInteger("default-batch-num-messages", 1000);

    private static final int DEFAULT_QUEUE_BUFFERING_MAX_MS =
            Integer.getInteger("default-queue-buffering-max-ms", 500);


    final byte[] partitionKey = new byte[4];

    private static String brokers = ServerConfig.getInstance().getKakfaUpBrokers();

    private KafkaProducer(){
        this(brokers,0,0,0,true);
    }

    /**
     *
     * @param brokers
     * @param ack
     * @param batchNum
     * @param batchMaxms
     * @param async
     */
    private KafkaProducer(final String brokers, final int ack, final int batchNum,
                          final int batchMaxms, final boolean async) {
        Preconditions.checkNotNull(brokers, "brokers cannot be null");

        // set props
        final Properties props = new Properties();
        props.put("metadata.broker.list", brokers.trim());
        props.put("message.send.max.retries", "3");
        if ((ack == 0) && async) {
            props.put("producer.type", "async");
            props.put("batch.num.messages", (batchNum == 0 ?
                    DEFAULT_BATCH_NUM__MESSAGES : batchNum) + "");
            props.put("queue.buffering.max.ms", (batchMaxms == 0 ?
                    DEFAULT_QUEUE_BUFFERING_MAX_MS : batchMaxms) + "");
        } else {
            props.put("producer.type", "sync");
        }
        props.put("request.required.acks", String.valueOf(ack));

        log.info("kafka props:{}", props);
        config = new ProducerConfig(props);

        //
        producer = new Producer<byte[], byte[]>(config);
    }
    public void sendMsg(final String topic, final byte[] msg){
        sendMsg(topic, msg, "");
    }


    public void sendMsg(final String topic, final byte[] msg,String partKey) {
        try {
            byte[] key = CommonUtil.getUUID().getBytes();

            KeyedMessage<byte[], byte[]> data =
                    new KeyedMessage<byte[], byte[]>(topic, key,partKey, msg);

            log.info("Sending to kafka topic={}, data={}",topic, msg);
            producer.send(data);
        } catch (final Throwable e) {
            log.error("error in sending to kafka: " + msg, e);
        }
    }

    public void produceMessageList(final String topic, List<byte[]> datas) {

        List<KeyedMessage<byte[], byte[]>> messageList = new ArrayList<KeyedMessage<byte[], byte[]>>();

        for (byte[] data : datas) {
            byte[] key = CommonUtil.getUUID().getBytes();

            messageList.add(new KeyedMessage<byte[], byte[]>(topic, key, data));
        }
        log.info("Sending to kafka topic={}, data={}", topic, datas);
        producer.send(messageList);
    }


    public void shutdown() {
        if (producer != null) {
            producer.close();
        }
    }

    // 线程安全单例
    //  1、可以实现延迟加载的功能，只有在调用getInstance（）的方法才会创建单例对象，
    // 并且是通过类加载器机制保证值创建一个单例对象；
    //
    //  2、JVM在类的初始化阶段（即在Class被加载后，且被线程使用之前），会执行类的初始化。
    // 在执行类的初始化期间，JVM会去获取一个锁。这个锁可以同步多个线程对同一个类的初始化。
    //
    //  3、对于Java类加载机制来说，当第一次访问类的静态字段的时候，会触发类加载，并且同
    // 一个类只加载一次。静态内部类也是如此，只会被加载一次，类加载过程由类加载器负责加锁，
    // 从而保证线程安全。
    private static class SingletoHolder{
        static final KafkaProducer instance = new KafkaProducer();
    }

    public static KafkaProducer getInstance(){
        return SingletoHolder.instance;
    }
}
