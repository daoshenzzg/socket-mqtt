package com.yb.socket.service.normal;

import com.alibaba.fastjson.JSONObject;
import com.yb.socket.codec.JsonDecoder;
import com.yb.socket.codec.JsonEncoder;
import com.yb.socket.pojo.Request;
import com.yb.socket.service.WrappedChannel;
import com.yb.socket.service.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 18:44
 */
public class ServerTest {
    private static final Logger logger = LoggerFactory.getLogger(ServerTest.class);

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.setPort(8000);
        server.setCheckHeartbeat(false);
        server.addEventListener(new JsonEchoMessageEventListener());
        server.addChannelHandler("decoder", new JsonDecoder());
        server.addChannelHandler("encoder", new JsonEncoder());
        server.bind();

        //模拟推送
        JSONObject message = new JSONObject();
        message.put("action", "echo");
        message.put("message", "this is yb push message!");

        Request request = new Request();
        request.setSequence(0);
        request.setMessage(message);
        while (true) {
            if (server.getChannels().size() > 0) {
                logger.info("模拟推送消息");
                for (WrappedChannel channel : server.getChannels().values()) {
                    channel.send(request);
                    Thread.sleep(5000L);
                }

            }
        }
    }
}
