package com.venusiot.vehicle.message.en;

import java.util.Map;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 14:15 2018/8/23
 * Modified By:
 */
public class NullEncoder implements NodeMsgEncoder<Object> {
    @Override
    public boolean encode(Map<String, Object> json, Object nodeMessage) {
        return false;
    }
}
