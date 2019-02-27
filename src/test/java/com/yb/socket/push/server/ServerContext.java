package com.yb.socket.push.server;

import org.springframework.context.ApplicationContext;

/**
 * @author daoshenzzg@163.com
 * @date 2019/2/26 16:32
 */
public class ServerContext {

    private ServerContext() {
    }

    private static ServerContext instance = new ServerContext();

    public static ServerContext getContext() {
        return instance;
    }

    private ApplicationContext app;

    public ApplicationContext getApp() {
        return app;
    }

    public void setApp(ApplicationContext app) {
        this.app = app;
    }
}
