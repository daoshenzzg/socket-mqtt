package com.mgtv.socket.center;

import com.mgtv.socket.codec.JsonDecoder;
import com.mgtv.socket.codec.JsonEncoder;
import com.mgtv.socket.service.server.Server;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/7 22:18
 */
public class CenterMock2 {

    public static void main(String[] args) {

        Server server = new Server();
        server.setPort(9010);
        server.setCheckHeartbeat(false);

        server.addEventListener(new CenterMockMessageEventListener());
        server.addChannelHandler("decoder", new JsonDecoder());
        server.addChannelHandler("encoder", new JsonEncoder());

        server.bind();
    }
}
