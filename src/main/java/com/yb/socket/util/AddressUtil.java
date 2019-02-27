package com.yb.socket.util;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:42
 */
public class AddressUtil {

    public static String getLocalIp() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            return address.getHostAddress().toString();
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static SocketAddress[] parseAddress(String addressArray) {
        if (StringUtils.isEmpty(addressArray)) {
            throw new IllegalArgumentException("addressArray can not be null or empty.");
        }

        String[] array = addressArray.split(",");
        SocketAddress[] addresses = new InetSocketAddress[array.length];
        for (int i = 0; i < array.length; i++) {
            String address = array[i];
            String[] parts = address.split(":");
            if (parts.length == 2) {
                addresses[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            } else {
                throw new IllegalArgumentException("address " + address + " is invalid.");
            }
        }

        return addresses;
    }

    /**
     * @param portArray
     * @return int[] 返回类型
     * @Title: parsePort
     * @Description: 解析端口
     */
    public static int[] parsePort(String portArray) {
        if (StringUtils.isEmpty(portArray)) {
            throw new IllegalArgumentException("portArray can not be null or empty.");
        }

        String[] array = portArray.split(",");
        int[] ports = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ports[i] = Integer.parseInt(array[i].trim());
        }

        return ports;
    }
}
