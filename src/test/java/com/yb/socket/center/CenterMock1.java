package com.yb.socket.center;

import com.yb.socket.codec.JsonDecoder;
import com.yb.socket.codec.JsonEncoder;
import com.yb.socket.service.server.Server;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/4 14:47
 */
public class CenterMock1 {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.setPort(9000);
        server.setCheckHeartbeat(false);
        server.addChannelHandler("decoder", new JsonDecoder());
        server.addChannelHandler("encoder", new JsonEncoder());
        server.addEventListener(new com.yb.socket.center.CenterMockMessageEventListener());
        server.bind();
    }
}
