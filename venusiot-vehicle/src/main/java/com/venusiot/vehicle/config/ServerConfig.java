package com.venusiot.vehicle.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 10:36 2018/8/16
 * Modified By:
 */
public final class ServerConfig {
    private static final Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    final Properties props = new Properties();


    // multithread singleton
    private static class SingletonHolder{
        static final ServerConfig instance = new ServerConfig();
    }

    public static ServerConfig getInstance(){
        return SingletonHolder.instance;
    }

    //
    private ServerConfig(){
        final String configPath = System.getProperty("app.path", null);
        final File fileConfig = new File(configPath, "config/vehicle.conf");

        logger.info("config.path [{}]", configPath);
        logger.info("config file path [{}]", fileConfig.getAbsolutePath());
        if (!fileConfig.exists()) {
            logger.error("can not found config file.. exiting...");
            System.exit(0);
        }

        InputStream input = null;
        try {
            input = new FileInputStream(fileConfig);
            props.load(input);
        }
        catch (IOException e){
            logger.error("read config file error.");
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getKakfaUpBrokers(){
        return props.getProperty(Constants.ConfigKey.KAFKA_BROKERS);
    }

    public String getKafkaUpTopicName(){
        return props.getProperty(Constants.ConfigKey.KAFKA_UP_TOPIC);
    }

    public String getKafkaDownGroupId(){
        return props.getProperty(Constants.ConfigKey.KAFKA_DOWN_GROUPID);
    }

    public String getKafkaDownTopicName(){
        return props.getProperty(Constants.ConfigKey.KAFKA_DOWN_TOPIC);
    }

    public String getKafkaDownZks(){
        return props.getProperty(Constants.ConfigKey.KAFKA_ZKS);
    }

    public String getKafkaDownTopicParatitions(){
        return props.getProperty(Constants.ConfigKey.KAFKA_DOWN_TOPIC_PART);
    }


    public String getRosHost(){
        return props.getProperty(Constants.ConfigKey.ROS_HOST);
    }

    public String getRosUri(){
        return props.getProperty(Constants.ConfigKey.ROS_URI);
    }

    public String getRosNodes(){
        return props.getProperty(Constants.ConfigKey.ROS_START_NODES);
    }
}
