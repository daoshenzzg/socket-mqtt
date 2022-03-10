package com.yb.socket.center;

import com.yb.socket.codec.JsonDecoder;
import com.yb.socket.codec.JsonEncoder;
import com.yb.socket.service.server.Server;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/7 22:18
 */
public class CenterMock2 {

    public static void main(String[] args) {

        Server server = new Server();
        server.setPort(9010);
        server.setCheckHeartbeat(false);
        server.addChannelHandler("decoder", JsonDecoder::new);
        server.addChannelHandler("encoder", JsonEncoder::new);
        server.addEventListener(new com.yb.socket.center.CenterMockMessageEventListener());
        server.bind();
    }
}
