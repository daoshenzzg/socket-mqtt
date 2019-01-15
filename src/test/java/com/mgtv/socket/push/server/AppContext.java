package com.mgtv.socket.push.server;

import org.springframework.context.ApplicationContext;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/14 19:16
 */
public class AppContext {

    private AppContext() {
    }

    private static AppContext instance = new AppContext();

    public static AppContext getInstance() {
        return instance;
    }

    private ApplicationContext context;

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public ApplicationContext getContext() {
        return context;
    }
}
