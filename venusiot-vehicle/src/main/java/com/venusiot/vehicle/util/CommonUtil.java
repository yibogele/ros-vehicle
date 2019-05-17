package com.venusiot.vehicle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 14:47 2018/8/22
 * Modified By:
 */
public class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private CommonUtil(){}


    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }
}
