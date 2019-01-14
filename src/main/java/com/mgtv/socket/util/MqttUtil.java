package com.mgtv.socket.util;

import com.mgtv.socket.pojo.MqttRequest;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/14 10:24
 */
public class MqttUtil {

    public MqttUtil() {
    }

    /**
     * 封装MqttPublishMessage
     *
     * @param topic
     * @param request
     * @return
     */
    public static MqttPublishMessage packPubMessage(String topic, MqttRequest request) {
        MqttPublishMessage pubMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.PUBLISH,
                        request.isDup(),
                        request.getQos(),
                        request.isRetained(),
                        0),
                new MqttPublishVariableHeader(topic, 0),
                Unpooled.buffer().writeBytes(request.getPayload()));
        return pubMessage;
    }
}
