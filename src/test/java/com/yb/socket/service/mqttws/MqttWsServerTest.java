package com.yb.socket.service.mqttws;

import com.alibaba.fastjson.JSONObject;
import com.yb.socket.pojo.MqttRequest;
import com.yb.socket.service.SocketType;
import com.yb.socket.service.WrappedChannel;
import com.yb.socket.service.mqtt.EchoMessageEventListener;
import com.yb.socket.service.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQTT web socket
 * @author daoshenzzg@163.com
 * @date 2018/12/30 18:41
 */
public class MqttWsServerTest {

    private static final Logger logger = LoggerFactory.getLogger(MqttWsServerTest.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.setPort(8000);
        server.addEventListener(new EchoMessageEventListener());
        server.setSocketType(SocketType.MQTT_WS);
        server.bind();

        //模拟推送
        String message = "this is a web socket message!";
        MqttRequest mqttRequest = new MqttRequest((message.getBytes()));
        while (true) {
            if (server.getChannels().size() > 0) {
                logger.info("模拟推送消息");
                for (WrappedChannel channel : server.getChannels().values()) {
                    server.send(channel, "yb/notice/", mqttRequest);
                }
            }
            Thread.sleep(1000L);
        }
    }
}
