package com.mgtv.socket.pojo;

import java.io.Serializable;

/**
 * 心跳消息
 *
 * @author zhiguang@mgtv.com
 * @date 2018/12/30 15:19
 */
public class Heartbeat extends BaseMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final byte[] BYTES = new byte[0];

    private static Heartbeat instance = new Heartbeat();

    public static Heartbeat getSingleton() {
        return instance;
    }

    private Heartbeat() {
    }
}
