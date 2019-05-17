package com.venusiot.vehicle.mock;

import com.core.venusiot.common.dto.VenusMessage;
import com.google.common.base.Preconditions;
import com.venusiot.vehicle.kafka.KafkaProducer;
import com.venusiot.vehicle.message.AvroMessageSerializer;
import com.venusiot.vehicle.message.CommonVenusMsg;
import com.venusiot.vehicle.util.JsonUtil;
import com.venusiot.vehicle.util.PublishThreadManager;
import geometry_msgs.Twist;
import geometry_msgs.Vector3;
import org.ros.internal.message.RawMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 16:43 2018/8/25
 * Modified By:
 */
public class MockNodeTest {
    private static Logger logger = LoggerFactory.getLogger(MockNodeTest.class);

    public static void testSubNode(){
        PublishThreadManager.execute(new Runnable() {
            @Override
            public void run() {
                while (true){

                    std_msgs.String strMsg = buildGpsMessage();
                    strMsg.setData("39.7403591667|116.561780167");

                    VenusMessage venusMessage = CommonVenusMsg.get();
                    if (venusMessage != null) {

                        String jsonString = JsonUtil.obj2json(strMsg);
                        // 构造一个json对象
                        /**
                         * JsonObject obj = new JsonObject();
                         *
                         *         //addProperty是添加属性（数值、数组等）；add是添加json对象
                         *
                         *         obj.addProperty("cat", "it");
                         *
                         *         obj.addProperty("pop", true);
                         *
                         *         JsonArray array = new JsonArray();
                         *
                         *
                         *
                         *         JsonObject lan1 = new JsonObject();
                         *
                         *         lan1.addProperty("id", 1);
                         *
                         *         lan1.addProperty("name","Java");
                         *
                         *         lan1.addProperty("ide", "Eclipse");
                         *
                         *         array.add(lan1);
                         *
                         *
                         *
                         *         JsonObject lan2 = new JsonObject();
                         *
                         *         lan2.addProperty("id", 2);
                         *
                         *         lan2.addProperty("name","swift");
                         *
                         *         lan2.addProperty("ide", "Xcode");
                         *
                         *         array.add(lan2);
                         *
                         *
                         *
                         *         JsonObject lan3 = new JsonObject();
                         *
                         *         lan3.addProperty("id", 3);
                         *
                         *         lan3.addProperty("name","C#");
                         *
                         *         lan3.addProperty("ide", "VisualStudio");
                         *
                         *         array.add(lan3);
                         *
                         *
                         *
                         *         obj.add("languages", array);
                         *
                         *         //控制台输出
                         *
                         *         System.out.println(obj.toString()
                         */

                        venusMessage.setBody(jsonString);
                    }

                    String topic = "router_" + venusMessage.getProductKey();
                    try {
                        KafkaProducer.getInstance().sendMsg(
                                topic,
                                venusMessage.toString().getBytes());
//                                AvroMessageSerializer.serialize(venusMessage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    logger.info("topic: {}", topic);


                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private static std_msgs.String buildGpsMessage(){
        return new std_msgs.String(){
            private String data;

            @Override
            public RawMessage toRawMessage() {
                return null;
            }

            @Override
            public String getData() {
                return data;
            }

            @Override
            public void setData(String s) {
                data = s;
            }
        };
    }

    public static void testVelocityPubNode(){
//        PublishThreadManager.execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }


    private static Twist buildVelocityMessage(){
        Twist msg = new Twist() {
            Vector3 vector3Line = new Vector3() {
                private double x;
                private double y;
                private double z;

                @Override
                public double getX() {
                    return x;
                }

                @Override
                public void setX(double v) {
                    x = v;
                }

                @Override
                public double getY() {
                    return y;
                }

                @Override
                public void setY(double v) {
                    y = v;
                }

                @Override
                public double getZ() {
                    return z;
                }

                @Override
                public void setZ(double v) {
                    z = v;
                }

                @Override
                public RawMessage toRawMessage() {
                    return null;
                }
            };
            Vector3 vector3Angular = vector3Line;

            @Override
            public Vector3 getLinear() {
                return vector3Line;
            }

            @Override
            public void setLinear(Vector3 vector3) {
                vector3Line = vector3;
            }

            @Override
            public Vector3 getAngular() {
                return vector3Angular;
            }

            @Override
            public void setAngular(Vector3 vector3) {
                vector3Angular = vector3;
            }

            @Override
            public RawMessage toRawMessage() {
                return null;
            }
        };


        return msg;
    }

    private static void setupNodeMessage(Twist cmd, double linearVelocityX, double linearVelocityY, double angularVelocityZ) {
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
