package com.yb.socket.pojo.protocol;

import com.yb.socket.pojo.Request;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 * 请求消息
 *
 * @author daoshenzzg@163.com
 * @date 2022-04-29 15:14
 */
public class ProtocolRequest extends Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private byte version;
    private byte messageType;
    private int bodyLength;
    private byte[] reserved = new byte[10];
    private Map<String, Object> body;

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getReserved() {
        return reserved;
    }

    public void setReserved(byte[] reserved) {
        this.reserved = reserved;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ProtocolRequest{" +
                "version=" + version +
                ", messageType=" + messageType +
                ", bodyLength=" + bodyLength +
                ", reserved=" + Arrays.toString(reserved) +
                ", body=" + body +
                '}';
    }
}
