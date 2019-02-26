package com.mgtv.socket.push.server;

import com.alibaba.fastjson.JSONObject;
import com.mgtv.socket.pojo.MqttRequest;
import com.mgtv.socket.service.SocketType;
import com.mgtv.socket.service.WrappedChannel;
import com.mgtv.socket.service.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author zhiguang@mgtv.com
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
        server.addEventListener(new TestMqttMessageEventListener());
        server.setSocketType(SocketType.MQTT);
        server.bind();

        ApplicationContext context = new ClassPathXmlApplicationContext("subscribe.xml");
        ServerContext.getContext().setApp(context);

        SubscribeServer subscribeServer = (SubscribeServer) context.getBean("subscribeServer");

        //模拟推送
        JSONObject message = new JSONObject();
        message.put("action", "echo");
        message.put("message", "this is mgtv push message!");

        MqttRequest mqttRequest = new MqttRequest((message.toString().getBytes()));

        String topic = "mgtv/notice/";
        while (true) {
            List<String> channelIds = subscribeServer.getChannelByTopic(topic);
            if (channelIds != null && channelIds.size() > 0) {
                logger.info("模拟推送消息");
                for (String channelId : channelIds) {
                    WrappedChannel channel = server.getChannel(channelId);
                    server.send(channel, "mgtv/notice/", mqttRequest);
                }
            }
            Thread.sleep(1000L);
        }
    }
}
