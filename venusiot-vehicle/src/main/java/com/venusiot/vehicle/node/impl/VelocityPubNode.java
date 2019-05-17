package com.venusiot.vehicle.node.impl;

import com.google.common.base.Preconditions;
import com.venusiot.vehicle.config.Constants;
import com.venusiot.vehicle.node.base.AbstractPubNode;
import geometry_msgs.Twist;

import java.util.Map;

//import com.venusiot.vehicle.node.base.AbstractSubPubNode;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 9:34 2018/8/22
 * Modified By:
 */
public class VelocityPubNode extends AbstractPubNode<Twist> {
    public VelocityPubNode(){
        super(Constants.TopicName.VELOCITY, Constants.TopicType.VELOCITY);
    }

    //
//    public static VelocityPubNode newNode(String topicName, String topicType){
//        VelocityPubNode velocityNode = new VelocityPubNode();
//        velocityNode.setTopicName(topicName);
//        velocityNode.setTopicType(topicType);
//
//        return velocityNode;
//    }


    @Override
    public Twist buildNodeMessage(Map<String, Object> map) {
        Twist message = publisher.newMessage();

        String x = (String)map.get("x");
        String y = (String)map.get("y");
        String z = (String)map.get("z");

        int ix = Integer.parseInt(x);
        int iy = Integer.parseInt(y);
        int iz = Integer.parseInt(z);

        setupCmdMessage(message, ix,iy,iz);
//        setupCmdMessage(message, 1,0,1);

        // TODO
        // 可以调用对应的Encoder来设置message
        //
//        if(NodeMsgEncoderFactory.get(NodeType.VELOCITY).encode(map, message))
//        {
//            return message;
//        }



        return message;
    }

    private void setupCmdMessage(Twist cmd, double linearVelocityX, double linearVelocityY, double angularVelocityZ){
        Preconditions.checkNotNull(cmd, "Twist cmd is null");

        float scale = 1.0f;
        cmd.getLinear().setX(linearVelocityX * scale);
        cmd.getLinear().setY(-linearVelocityY * scale);
        cmd.getLinear().setZ(0.0);
        cmd.getAngular().setX(0.0);
        cmd.getAngular().setY(0.0);
        cmd.getAngular().setZ(-angularVelocityZ);
    }


}
