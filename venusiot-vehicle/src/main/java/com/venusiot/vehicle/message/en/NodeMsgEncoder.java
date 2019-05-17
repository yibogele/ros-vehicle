package com.venusiot.vehicle.message.en;

import java.util.Map;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 13:43 2018/8/23
 * Modified By:
 */
public interface NodeMsgEncoder<T> {
    boolean encode(Map<String, Object> dataMap, T nodeMessage);
}
