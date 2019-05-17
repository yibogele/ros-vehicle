package com.venusiot.vehicle;

import com.venusiot.vehicle.config.Constants;
import com.venusiot.vehicle.kafka.KafkaSubcriber;
import com.venusiot.vehicle.mock.MockNodeTest;
import com.venusiot.vehicle.node.NodesRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 9:27 2018/8/22
 * Modified By:
 */
public class VehicleGateway {
    private static Logger logger = LoggerFactory.getLogger(VehicleGateway.class);

     public static void main(String[] args) {


        // mock
        if (Constants.Test.isTest){
            new KafkaSubcriber().start();

            MockNodeTest.testSubNode();
            MockNodeTest.testVelocityPubNode();

        }else{

            NodesRunner.instance().startAllNodes();

            new KafkaSubcriber().start();
            // shutdown, clear resource
//            PublishThreadManager.shutdown();
        }
    }
}
