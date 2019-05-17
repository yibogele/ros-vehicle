package com.venusiot.vehicle.message;

import com.venusiot.vehicle.node.NodeType;
import com.venusiot.vehicle.node.NodesRunner;
import com.venusiot.vehicle.node.base.AbstractPubNode;
import com.venusiot.vehicle.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 17:20 2018/8/22
 * Modified By:
 */
public class NodeMsgHandler {
    private static Logger logger = LoggerFactory.getLogger(NodeMsgHandler.class);

    public static void handlePubMsg(String jsonMsg){
        logger.info("handlePubMsg message: {}", jsonMsg);

        try {
            Map<String, Object> msgMap = JsonUtil.json2map(jsonMsg);
//            Map<String, Object> nodeMsgMap = (Map<String, Object>) dataMap.get("data");

            String strNodeType = (String)msgMap.get("type");

            NodeType nodeType = NodeType.valueOf(strNodeType.toUpperCase(Locale.ENGLISH));

            AbstractPubNode pubNode = (AbstractPubNode)NodesRunner.instance().getNodeByType(nodeType);

            pubNode.publish(msgMap);

        } catch (Exception e) {
            logger.error("handlePubMsg error: {}", e);
            e.printStackTrace();
        }
        // 根据消息体 构造encoder

        //
    }

    public static void handlePubMsgTest(String message){
        logger.info("handlePubMsgTest message = {}", message);

        try {
            Map<String, Object> dataMap = JsonUtil.json2map(message);
//            Map<String, Object> nodeMsgMap = (Map<String, Object>) dataMap.get("data");

            String strNodeType = (String)dataMap.get("type");

            logger.info("handlePubMsgTest: nodeType = {}", strNodeType);

//            NodeType nodeType = NodeType.valueOf(strNodeType);
//
//            AbstractPubNode pubNode = (AbstractPubNode)NodesRunner.instance().getNodeByType(nodeType);
//            pubNode.publish(nodeMsgMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
