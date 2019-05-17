package com.venusiot.vehicle.node.base;

import com.core.venusiot.common.dto.VenusMessage;
import com.google.common.base.Preconditions;
import com.venusiot.vehicle.kafka.KafkaProducer;
import com.venusiot.vehicle.util.CommonUtil;
import com.venusiot.vehicle.util.PublishThreadManager;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 14:01 2018/8/22
 * Modified By:
 */
public abstract class AbstractSubPubNode<T> extends AbstractNodeMain {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final BlockingQueue<T> queue = new ArrayBlockingQueue<>(1024);

    protected Publisher<T> publisher;
    protected Subscriber<T> subscriber;

    protected String topicType;
    protected String topicName;


    protected AbstractSubPubNode(String topicName, String topicType){
        setTopicName(topicName);
        setTopicType(topicType);

    }


    public void publish(Map<String, Object> msgMap){
        Preconditions.checkNotNull(msgMap, "Message can not be null");

        final T cmdObj = buildNodeMessage(msgMap);

        PublishThreadManager.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("push node message:[{}] to topic:[{}]", cmdObj, topicName);

                    queue.put(cmdObj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    /**
     * 根据字符串解析出Node的消息体,注意T必须经由publisher.newMessage()创建
     * @param msgMap
     * @return
     */
    public abstract T buildNodeMessage(Map<String, Object> msgMap);

    /**
     *
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

        // publisher
        publisher = connectedNode.newPublisher(topicName, topicType);
//        message = publisher.newMessage();
        connectedNode.executeCancellableLoop(new CancellableLoop() {

            @Override
            protected void loop() throws InterruptedException {

                T msg = queue.take();
                logger.info("Node: {} received new message: {}, publishing to topic: {}",
                        AbstractSubPubNode.this.getDefaultNodeName(),
                        msg,
                        AbstractSubPubNode.this.topicName);

                publisher.publish(msg);
            }
        });


        // subscriber
        subscriber = connectedNode.newSubscriber(topicName, topicType);
        subscriber.addMessageListener(new MessageListener<T>() {
            @Override
            public void onNewMessage(T t) {
                logger.info("receive new message: [{}] - [{}]", t.getClass(), t);
                // send to kafka

//                String jsonString = JsonUtil.obj2json(t);
                VenusMessage venusMessage = buildVenusMessage(t);

                KafkaProducer.getInstance().sendMsg(
                        "router_"+venusMessage.getProductKey(),
                        venusMessage.toString().getBytes()
                );
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
