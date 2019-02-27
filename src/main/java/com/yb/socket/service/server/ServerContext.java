package com.yb.socket.service.server;

/**
 * 全局共享的Server服务
 *
 * @author daoshenzzg@163.com
 * @date 2018/12/30 14:48
 */
public class ServerContext {

    private ServerContext() {
    }

    private static ServerContext instance = new ServerContext();

    public static ServerContext getContext() {
        return instance;
    }

    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
