package com.venusiot.vehicle.node.impl;

import com.core.venusiot.common.dto.VenusMessage;
import com.venusiot.vehicle.config.Constants;
import com.venusiot.vehicle.message.CommonVenusMsg;
import com.venusiot.vehicle.node.base.AbstractSubNode;
import com.venusiot.vehicle.util.JsonUtil;
//import std_msgs.String;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 16:00 2018/8/25
 * Modified By:
 */
public class GpsSubNode extends AbstractSubNode<std_msgs.String> {
    public GpsSubNode(){
        super(Constants.TopicName.GPS, Constants.TopicType.GPS);
    }

    @Override
    protected VenusMessage buildVenusMessage(std_msgs.String msg) {
        VenusMessage venusMessage = CommonVenusMsg.get();
        if (venusMessage != null) {

            String jsonString = JsonUtil.obj2json(msg.getData());
            logger.info("buildVenusMessage: jsonString - [{}]", jsonString);
            venusMessage.setBody(jsonString);
        }

        return venusMessage;
    }
}
