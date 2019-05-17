package com.venusiot.vehicle.kafka;

import com.core.venusiot.common.dto.DownVenusMessage;
import com.venusiot.vehicle.config.Constants;
import com.venusiot.vehicle.config.ServerConfig;
import com.venusiot.vehicle.message.AvroMessageSerializer;
import com.venusiot.vehicle.message.NodeMsgHandler;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Author: Will Fan
 * Description:
 * Date: Created in 17:09 2018/8/22
 * Modified By:
 */
public class KafkaSubcriber {

    private ExecutorService executor;
    private ConsumerConnector consumer;
    private ConsumerConfig config;
    private final int threadNum = 1;
    private Logger log = LoggerFactory.getLogger(KafkaSubcriber.class);
    public KafkaSubcriber() {
        init();
    }

    private void init() {
        Properties props = new Properties();
        props.put("zookeeper.connect", ServerConfig.getInstance().getKafkaDownZks());
        props.put("group.id", ServerConfig.getInstance().getKafkaDownGroupId());

        props.put("zookeeper.session.timeout.ms", "10000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        config = new ConsumerConfig(props);

    }

    public void start() {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
        // topicCountMap and count thread num
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(ServerConfig.getInstance().getKafkaDownTopicName(),
                Integer.parseInt(ServerConfig.getInstance().getKafkaDownTopicParatitions()));
        // 支持分布式
        executor = Executors.newFixedThreadPool(threadNum);
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
        for (String topic : messageStreams.keySet()) {
            for (final KafkaStream<byte[], byte[]> stream : messageStreams.get(topic)) {
                executor.submit(new Runnable() {
                    public void run() {
                        try {
                            ConsumerIterator<byte[], byte[]> it = stream.iterator();
                            while (it.hasNext()) {
                                byte[] message = it.next().message();
                                log.info("kafka received bytes :{}", new String(message));
                                DownVenusMessage downVenusMessage = AvroMessageSerializer.deserialize(message);
                                log.info("kafka received msg: {}", downVenusMessage);

                                String cmdMessage = downVenusMessage.getBody().toString();
                                if (Constants.Test.isTest)
                                {

                                    NodeMsgHandler.handlePubMsgTest(cmdMessage);
                                }else
                                {

                                    NodeMsgHandler.handlePubMsg(cmdMessage);
                                }
                            }
                        } catch (Exception e) {
                            log.error("kafka client error: {}", e);
                            log.error(e.getMessage(), e);
                        }
                    }

                });
            }
        }
    }
}
