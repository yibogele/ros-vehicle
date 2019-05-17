package com.venusiot.vehicle.message;

import com.venusiot.vehicle.message.en.NodeMsgEncoder;
import com.venusiot.vehicle.message.en.NullEncoder;
import com.venusiot.vehicle.message.en.VelocityEncoder;
import com.venusiot.vehicle.node.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 14:12 2018/8/23
 * Modified By:
 */
public class NodeMsgEncoderFactory {
    private static Logger logger = LoggerFactory.getLogger(NodeMsgEncoderFactory.class);

    private NodeMsgEncoderFactory(){}

    public static NodeMsgEncoder get(NodeType nodeType){
        switch (nodeType){
            case VELOCITY:
                return new VelocityEncoder();
            default:
                return new NullEncoder();
        }
    }
}
