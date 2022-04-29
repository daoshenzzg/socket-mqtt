package com.yb.socket.service.protocol;

import com.yb.socket.codec.protocol.ProtocolDecoder;
import com.yb.socket.codec.protocol.ProtocolEncoder;
import com.yb.socket.listener.DefaultExceptionListener;
import com.yb.socket.service.server.Server;

/**
 * @author daoshenzzg@163.com
 * @date 2022-04-29 15:52
 */
public class ServerTest {

    public static void main(String[] args) {
        Server server = new Server();
        server.setPort(8000);
        server.setCheckHeartbeat(true);
        server.setOpenStatus(true);
        server.setStatusPort(15001);

        server.addEventListener(new ProtocolEchoMessageEventListener());
        server.addEventListener(new DefaultExceptionListener());
        server.addChannelHandler("decoder", ProtocolEncoder::new);
        server.addChannelHandler("encoder", ProtocolDecoder::new);

        server.bind();
    }

}
