package com.mgtv.socket.center;

import com.alibaba.fastjson.JSON;
import com.mgtv.socket.pojo.MqttRequest;
import com.mgtv.socket.push.server.AppContext;
import com.mgtv.socket.push.server.ChannelDao;
import com.mgtv.socket.push.server.MqttMessageEventListener;
import com.mgtv.socket.service.SocketType;
import com.mgtv.socket.service.WrappedChannel;
import com.mgtv.socket.service.server.Server;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zhiguang@mgtv.com
 * @date 2018/12/30 18:41
 */
public class MqttServer1 {

    private static final Logger logger = LoggerFactory.getLogger(MqttServer1.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("activity.xml");
        AppContext.getInstance().setContext(context);

        ChannelDao channelDao = (ChannelDao)AppContext.getInstance().getContext().getBean("channelDao");

        Server server = new Server();
        server.setPort(8081);
        server.setOpenCount(true);
        server.setCheckHeartbeat(true);
        server.setCenterAddr("127.0.0.1:9000,127.0.0.1:9010");
        server.addEventListener(new MqttMessageEventListener());
        server.setSocketType(SocketType.MQTT);
        server.bind();

        //模拟推送
        JSONObject message = new JSONObject();
        message.put("action", "echo");
        message.put("message", "this is mgtv push message!");

        MqttRequest mqttRequest = new MqttRequest((message.toString().getBytes()));
        while (true) {
            if (server.getChannels().size() > 0) {
                logger.info("模拟推送消息:");
                logger.info(JSON.toJSONString(channelDao.getAll()));
                for (WrappedChannel channel : server.getChannels().values()) {
                    server.send(channel, "#", mqttRequest);
                    Thread.sleep(5000L);
                }
            }
        }
    }
}
