package com.venusiot.vehicle.node;

import com.google.common.base.Preconditions;
import com.venusiot.vehicle.config.ServerConfig;
import org.apache.commons.lang3.StringUtils;
import org.ros.node.AbstractNodeMain;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 13:47 2018/8/23
 * Modified By:
 */
public class NodesRunner {
    static final String URI_STR = "http://192.168.43.125:11311/";
    static final String HOST_STR = "192.168.43.87";

    private static Logger logger = LoggerFactory.getLogger(NodesRunner.class);

    private Map<NodeType, AbstractNodeMain> nodeMap = new HashMap<>();

    private String strHost;
    private String strUri;

    // masterUri
    private URI masterUri;

    // main executor
    private NodeMainExecutor nodeMainExecutor= DefaultNodeMainExecutor.newDefault();


    private NodesRunner(){
        if(initUri())
            configureNodes();
    }

    // multithread singleton
    private static class SingletonHolder{
        static final NodesRunner instance = new NodesRunner();
    }

    public static NodesRunner instance(){
        return NodesRunner.SingletonHolder.instance;
    }

    private boolean initUri() {
        try {
            strHost = ServerConfig.getInstance().getRosHost();
            strHost = StringUtils.isEmpty(strHost) ? HOST_STR : strHost;

            strUri = ServerConfig.getInstance().getRosUri();
            strUri = StringUtils.isEmpty(strUri) ? URI_STR : strUri;
            //机器车端的ip
            masterUri = new URI(strUri);
            return true;
        } catch (URISyntaxException e) {
//            throw new RosRuntimeException(e);
            logger.info("parse master uri failed: {}", e);
            return false;
        }
    }

    private boolean configureNodes(){
        String strNodes = ServerConfig.getInstance().getRosNodes();
        Preconditions.checkNotNull(strNodes, "Config start-ros-node is null!");

        String[] nodes = StringUtils.split(strNodes, ",");

        try {
            for (String node : nodes) {
                Preconditions.checkNotNull(node, "null node class name");
                AbstractNodeMain objNode = NodesBuilder.newInstance(node);

                nodeMap.put(NodeType.valueOf(node.toUpperCase(Locale.ENGLISH)), objNode);

            }
        } catch (ClassNotFoundException e) {
            logger.info("configureNodes: {}", e);
        } catch (InstantiationException e) {
            logger.info("configureNodes: {}", e);
        } catch (IllegalAccessException e) {
            logger.info("configureNodes: {}", e);
        }

        return nodeMap.size() == 0;
    }

//    private void getESData(){
//        Map<String, Object> esData = ElasticSearchUtil.getEsData(msg.getDevId());
//        if (esData != null){
//
//        }

//    }

    public AbstractNodeMain getNodeByType(NodeType nodeType){
        return nodeMap.get(nodeType);
    }

    public AbstractNodeMain getNodeByString(String node){

        NodeType nodeType = NodeType.valueOf(node.toUpperCase(Locale.ENGLISH));

        return getNodeByType(nodeType);
    }

    public void startAllNodes(){
        for (NodeType key : nodeMap.keySet()) {
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(strHost);
            nodeConfiguration.setMasterUri(masterUri);
            nodeConfiguration.setNodeName("venusiot/node/" + key.getId());

            nodeMainExecutor.execute(nodeMap.get(key), nodeConfiguration);
        }
    }
}
