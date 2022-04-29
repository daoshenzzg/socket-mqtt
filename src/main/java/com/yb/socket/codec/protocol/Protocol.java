package com.yb.socket.codec.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yb.socket.pojo.Heartbeat;
import com.yb.socket.pojo.protocol.ProtocolRequest;
import com.yb.socket.pojo.protocol.ProtocolResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author daoshenzzg@163.com
 * @date 2022-04-29 15:36
 */
public class Protocol {

    /**
     * 编码
     *
     * @param msg
     * @return
     * @throws Exception
     */
    public static ByteBuf encode(Object msg) throws Exception {
        ByteBuf totalBuf = Unpooled.buffer();

        if (msg instanceof Heartbeat) {
            byte[] header = new byte[20];
            totalBuf.writeBytes(header);
            return totalBuf;
        } else if (msg instanceof ProtocolRequest) {
            ProtocolRequest request = (ProtocolRequest) msg;
            byte[] body = packJson(request.getBody());

            // 组织header
            ByteBuf headBuf = Unpooled.buffer(20);
            headBuf.writeByte(request.getVersion());
            headBuf.writeByte(MessageTypeEnum.REQUEST.getType());
            headBuf.writeInt(body != null ? body.length : 0);
            headBuf.writeInt(request.getSequence());
            headBuf.writeBytes(request.getReserved());

            totalBuf.writeBytes(headBuf);
            if (ArrayUtils.isNotEmpty(body)) {
                totalBuf.writeBytes(body);
            }

            return totalBuf;
        } else if (msg instanceof ProtocolResponse) {
            ProtocolResponse response = (ProtocolResponse) msg;
            byte[] body = packJson(response.getBody());

            //组织header
            ByteBuf headBuf = Unpooled.buffer(20);
            headBuf.writeByte(response.getVersion());
            headBuf.writeByte(MessageTypeEnum.RESPONSE.getType());
            headBuf.writeInt(body != null ? body.length : 0);
            headBuf.writeInt(response.getSequence());
            headBuf.writeBytes(response.getReserved());

            totalBuf.writeBytes(headBuf);
            if (ArrayUtils.isNotEmpty(body)) {
                totalBuf.writeBytes(body);
            }

            return totalBuf;
        }

        return null;
    }

    /**
     * 解码
     *
     * @param buffer
     * @return
     * @throws Exception
     */
    public static Object decode(ByteBuf buffer) throws Exception {
        //读取header
        byte version = buffer.readByte();
        byte msgType = buffer.readByte();
        int bodyLength = buffer.readInt();
        int seqId = buffer.readInt();
        byte[] reserved = new byte[10];
        buffer.readBytes(reserved);

        MessageTypeEnum messageTypeEnum = MessageTypeEnum.get(msgType);
        if (messageTypeEnum == null) {
            throw new RuntimeException("unsupported messageType: " + msgType);
        }

        if (MessageTypeEnum.HEARTBEAT.equals(messageTypeEnum)) {
            return Heartbeat.getSingleton();
        }

        byte[] bodyBytes = new byte[bodyLength];
        buffer.readBytes(bodyBytes);

        Map<String, Object> body = unpackJson(bodyBytes);
        if (MessageTypeEnum.REQUEST.equals(messageTypeEnum)) {
            ProtocolRequest request = new ProtocolRequest();
            request.setVersion(version);
            request.setBodyLength(bodyLength);
            request.setSequence(seqId);
            request.setReserved(reserved);
            request.setBody(body);

            return request;
        } else if (MessageTypeEnum.RESPONSE.equals(messageTypeEnum)) {
            ProtocolResponse response = new ProtocolResponse();
            response.setVersion(version);
            response.setBodyLength(bodyLength);
            response.setSequence(seqId);
            response.setReserved(reserved);
            response.setBody(body);

            return response;
        }

        throw new RuntimeException("messageType must be [0,1,2].");
    }


    public static byte[] packJson(Map<String, Object> body) throws Exception {
        if (body == null) {
            return null;
        }
        return JSON.toJSONString(body).getBytes();
    }

    public static Map<String, Object> unpackJson(byte[] bodyBytes) throws Exception {
        String body = new String(bodyBytes);
        return jsonToMap(body);
    }

    private static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new HashMap<>();
        JSONObject json = JSON.parseObject(str);
        @SuppressWarnings("rawtypes")
        Iterator keys = json.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = json.get(key).toString();
            if (value.startsWith("{") && value.endsWith("}")) {
                map.put(key, jsonToMap(value));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }
}
