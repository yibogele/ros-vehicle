package com.venusiot.vehicle.message.en;

import com.google.common.base.Preconditions;
import geometry_msgs.Twist;

import java.util.Map;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 13:45 2018/8/23
 * Modified By:
 */
public class VelocityEncoder implements NodeMsgEncoder<Twist> {
    @Override
    public boolean encode(Map<String, Object> dataMap, Twist nodeMessage) {
        Preconditions.checkNotNull(dataMap, "dataMap is null");
        Preconditions.checkNotNull(nodeMessage, "nodeMessage is null");


        return false;
    }
}
