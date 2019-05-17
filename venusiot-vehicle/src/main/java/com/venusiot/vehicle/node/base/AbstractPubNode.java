package com.venusiot.vehicle.node.base;

import com.google.common.base.Preconditions;
import com.venusiot.vehicle.util.CommonUtil;
import com.venusiot.vehicle.util.PublishThreadManager;
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 15:48 2018/8/25
 * Modified By:
 */
public abstract class AbstractPubNode<T> extends AbstractNodeMain {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final BlockingQueue<T> queue = new ArrayBlockingQueue<>(1024);

    protected Publisher<T> publisher;
//    protected Subscriber<T> subscriber;

    protected String topicType;
    protected String topicName;


    protected AbstractPubNode(String topicName, String topicType){
        setTopicName(topicName);
        setTopicType(topicType);

    }

    /**
     * 根据字符串解析出Node的消息体,注意T必须经由publisher.newMessage()创建
     * @param msgMap
     * @return
     */
    public abstract T buildNodeMessage(Map<String, Object> msgMap);


    public void publish(Map<String, Object> cmdMsg){
        Preconditions.checkNotNull(cmdMsg, "Message can not be null");

        final T cmdObj = buildNodeMessage(cmdMsg);

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
                        AbstractPubNode.this.getDefaultNodeName(),
                        msg,
                        AbstractPubNode.this.topicName);

                publisher.publish(msg);
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
