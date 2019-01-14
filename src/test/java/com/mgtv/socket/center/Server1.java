package com.mgtv.socket.center;

import com.mgtv.socket.codec.JsonDecoder;
import com.mgtv.socket.codec.JsonEncoder;
import com.mgtv.socket.service.normal.JsonEchoMessageEventListener;
import com.mgtv.socket.service.server.Server;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/7 22:30
 */
public class Server1 {

    public static void main(String[] args) throws Exception{
        Server server = new Server();
        server.setPort(8000);
        server.setCheckHeartbeat(false);
        server.setCenterAddr("127.0.0.1:9000,127.0.0.1:9010");

        server.addEventListener(new JsonEchoMessageEventListener());
        server.addChannelHandler("decoder", new JsonDecoder());
        server.addChannelHandler("encoder", new JsonEncoder());

        server.bind();
    }
}
