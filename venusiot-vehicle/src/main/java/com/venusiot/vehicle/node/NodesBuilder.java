package com.venusiot.vehicle.node;

import org.ros.node.AbstractNodeMain;

import java.util.Locale;

/**
 * Author: Will Fan
 * Description:
 * Date: Created in 15:52 2018/8/22
 * Modified By:
 */
public class NodesBuilder {
    private static Class<? extends AbstractNodeMain> lookup(String name) {
        try {
            return NodeType.valueOf(name.toUpperCase(Locale.ENGLISH)).getBuilderClass();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    /**
     *
     * @param name 别名 (velocity)或全名(com.*.ClassName)
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static AbstractNodeMain newInstance(String name)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {

        Class<? extends AbstractNodeMain> clazz = lookup(name);
        if (clazz == null) {
            clazz = (Class<? extends AbstractNodeMain>) Class.forName(name);
        }
        return clazz.newInstance();
    }
}
