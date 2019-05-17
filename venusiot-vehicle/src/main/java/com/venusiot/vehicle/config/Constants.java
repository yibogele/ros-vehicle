package com.venusiot.vehicle.config;

import geometry_msgs.Twist;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 15:15 2018/8/21
 * Modified By:
 */
public class Constants {
    public interface Test{
        static final boolean isTest = false;
    }
    public interface ConfigKey {
        // 上行需要的配置
        static final String KAFKA_BROKERS = "kafka.brokers";
        static final String KAFKA_UP_TOPIC = "kafka.up.topic";

        // 下行需要的配置
        static final String KAFKA_ZKS = "kafka.zks";
        static final String KAFKA_DOWN_TOPIC = "kafka.down.topic";
        static final String KAFKA_DOWN_GROUPID = "kafka.down.groupid";
        static final String KAFKA_DOWN_TOPIC_PART = "kafka.down.topic.partitions";

        // 小车的配置
        static final String ROS_HOST = "ros.host";
        static final String ROS_URI = "ros.uri";
        static final String ROS_START_NODES = "ros.start.nodes";
    }
    public interface NodeName{
        static final String VELOCITY = "ctl_velocity";
    }

    public interface TopicName {
        static final String VELOCITY = "/cmd_vel";
        static final String GPS = "/gps_reader/gps_data";
    }

    public interface TopicType{
        static final String VELOCITY = Twist._TYPE;
        static final String GPS = std_msgs.String._TYPE;
    }
}
