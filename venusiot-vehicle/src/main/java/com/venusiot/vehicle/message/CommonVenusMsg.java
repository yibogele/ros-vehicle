package com.venusiot.vehicle.message;

import com.core.venusiot.common.dto.VenusMessage;
import com.core.venusiot.elasticsearch.ElasticSearchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 9:40 2018/8/24
 * Modified By:
 */
public class CommonVenusMsg {
    private static Logger logger = LoggerFactory.getLogger(CommonVenusMsg.class);

    public static VenusMessage get() {
        String deviceId = "test_vehicle_123";
//        logger.info("mock...................................");

        Map<String, Object> devData = ElasticSearchUtil.getEsData(deviceId);

        if (devData != null) {

            //验证通过
            VenusMessage venusMessage = new VenusMessage();
            //校验venusMessage
            venusMessage.setDeviceId(deviceId);
            venusMessage.setDeviceType(devData.get("deviceType").toString());
            venusMessage.setFeedId(devData.get("feedId").toString());
            venusMessage.setApiKey(devData.get("APIkey").toString());
            venusMessage.setProductKey(devData.get("productKey").toString());
            venusMessage.setDataType("1");
            venusMessage.setProtocolType(devData.get("protocolType").toString());
            venusMessage.setDateTime(System.currentTimeMillis() + "");
//            venusMessage.setCurMapType(devData.get("curMapType").toString());
//            venusMessage.setLongitude(devData.get("longitude").toString());
//            venusMessage.setLatitude(devData.get("latitude").toString());
//            venusMessage.setDirection(devData.get("direction").toString());
//            venusMessage.setAttachments(devData.get("attachments").toString());
//            venusMessage.setMessageType(devData.get("messageType").toString());
//            venusMessage.setAlarm(devData.get("alarm").toString());
//            venusMessage.setMnc(devData.get("mnc").toString());
//            venusMessage.setLac(devData.get("lac").toString());
//            venusMessage.setCellid(devData.get("cellid").toString());
//            venusMessage.setMessageId(devData.get("messageId").toString());
//            venusMessage.setVersion(devData.get("version").toString());
//            venusMessage.setSn(devData.get("sn").toString());


//            String strMsg = venusMessage.toString();
//            logger.info(venusMessage.toString());
//            //String strMsg = JSON.toJSONString(venusMessage);
//            StringBuilder sb=new StringBuilder();
//            String topic=sb.append("router").append("_").append(venusMessage.getProductKey()).toString();
//
            return venusMessage;
        }
        return null;
    }
}
