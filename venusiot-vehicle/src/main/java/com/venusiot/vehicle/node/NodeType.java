package com.venusiot.vehicle.node;

import com.venusiot.vehicle.node.impl.GpsSubNode;
import com.venusiot.vehicle.node.impl.VelocityPubNode;
import org.ros.node.AbstractNodeMain;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 16:19 2018/8/22
 * Modified By:
 */
public enum NodeType {
    NULL("null", null),
    VELOCITY("velocity",VelocityPubNode.class),
    GPS("gps", GpsSubNode.class),
    ;

    private String id;
    private final Class<? extends AbstractNodeMain> builderClass;

    private NodeType(String id, Class<? extends AbstractNodeMain> builderClass) {
        this.id = id;
        this.builderClass = builderClass;
    }

    public Class<? extends AbstractNodeMain> getBuilderClass() {
        return builderClass;
    }

    @Override
    public String toString() {
        return "NodeType{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId(){
        return id;
    }
}
