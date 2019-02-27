package com.yb.socket.service.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.yb.socket.pojo.MqttRequest;
import com.yb.socket.service.SocketType;
import com.yb.socket.service.WrappedChannel;
import com.yb.socket.service.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 18:41
 */
public class MqttServerTest {

    private static final Logger logger = LoggerFactory.getLogger(MqttServerTest.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.setPort(8000);
        server.setOpenCount(true);
        server.setCheckHeartbeat(true);
        server.setOpenStatus(true);
        server.addEventListener(new EchoMessageEventListener());
        server.setSocketType(SocketType.MQTT);
        server.bind();

        //模拟推送
        JSONObject message = new JSONObject();
        message.put("action", "echo");
        message.put("message", "this is yb push message!");

        MqttRequest mqttRequest = new MqttRequest((message.toString().getBytes()));
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
