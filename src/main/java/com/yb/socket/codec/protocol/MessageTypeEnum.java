package com.yb.socket.codec.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daoshenzzg@163.com
 * @date 2022-04-29 15:41
 */
public enum MessageTypeEnum {
    // 消息类型。0-心跳；1-请求；2-响应。
    HEARTBEAT((byte) 0),
    REQUEST((byte) 1),
    RESPONSE((byte) 2);

    private byte type;

    MessageTypeEnum(byte type) {
        this.type = type;
    }

    private static Map<Byte, MessageTypeEnum> map;

    private synchronized static void initMap() {
        map = new HashMap<>();
        for (MessageTypeEnum e : MessageTypeEnum.values()) {
            map.put(e.type, e);
        }
    }

    public static MessageTypeEnum get(byte type) {
        if (map == null) {
            initMap();
        }
        return map.get(type);
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
