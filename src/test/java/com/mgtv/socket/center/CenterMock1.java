package com.mgtv.socket.center;

import com.mgtv.socket.codec.JsonDecoder;
import com.mgtv.socket.codec.JsonEncoder;
import com.mgtv.socket.service.server.Server;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/4 14:47
 */
public class CenterMock1 {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.setPort(9000);
        server.setCheckHeartbeat(false);
        server.addChannelHandler("decoder", new JsonDecoder());
        server.addChannelHandler("encoder", new JsonEncoder());
        server.addEventListener(new CenterMockMessageEventListener());
        server.bind();
    }
}
