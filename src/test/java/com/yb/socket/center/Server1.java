package com.yb.socket.center;

import com.yb.socket.service.normal.JsonEchoMessageEventListener;
import com.yb.socket.service.server.Server;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/7 22:30
 */
public class Server1 {

    public static void main(String[] args) {
        Server server = new Server();
        server.setPort(8000);
        server.setCheckHeartbeat(false);
        server.setCenterAddr("127.0.0.1:9000,127.0.0.1:9010");
        server.addEventListener(new JsonEchoMessageEventListener());
        server.bind();
    }
}
