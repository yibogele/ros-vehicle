package com.venusiot.vehicle.node.base;

import com.core.venusiot.common.dto.VenusMessage;
import com.google.common.base.Preconditions;
import com.venusiot.vehicle.kafka.KafkaProducer;
import com.venusiot.vehicle.util.CommonUtil;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 15:48 2018/8/25
 * Modified By:
 */
public abstract class AbstractSubNode<T> extends AbstractNodeMain {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Subscriber<T> subscriber;

    protected String topicType;
    protected String topicName;

    protected AbstractSubNode(String topicName, String topicType){
        setTopicName(topicName);
        setTopicType(topicType);

    }
    /**
     * 构造VenusMessage, 发送到IOT平台
     * @param msg
     * @return
     */
    protected abstract VenusMessage buildVenusMessage(T msg);

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("venusiot/"+CommonUtil.getUUID());
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        logger.info("onStart: {}" + connectedNode);
        Preconditions.checkNotNull(topicName, "please set topic name first");
        Preconditions.checkNotNull(topicType, "Please set topic type first");

        // subscriber
        subscriber = connectedNode.newSubscriber(topicName, topicType);
        subscriber.addMessageListener(new MessageListener<T>() {
            @Override
            public void onNewMessage(T t) {
                logger.info("receive new message: [{}] - [{}]", t.getClass(), t);
                // send to kafka

//                String jsonString = JsonUtil.obj2json(t);
                VenusMessage venusMessage = buildVenusMessage(t);

                try {
                    KafkaProducer.getInstance().sendMsg(
                            "router_"+venusMessage.getProductKey(),
//                            AvroMessageSerializer.serialize(venusMessage)
                            venusMessage.toString().getBytes()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onShutdown(Node node) {
        logger.info("onShutdown: {}", node);
        super.onShutdown(node);
    }

    @Override
    public void onShutdownComplete(Node node) {
        logger.info("onShutdownComplete: {}", node);
        super.onShutdownComplete(node);
    }

    @Override
    public void onError(Node node, Throwable throwable) {
        logger.info("onError: {} - {}", node, throwable);
        super.onError(node, throwable);
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
