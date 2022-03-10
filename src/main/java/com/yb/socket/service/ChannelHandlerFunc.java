package com.mgtv.socket.service;

import io.netty.channel.ChannelHandler;

/**
 * @author zhiguang@mgtv.com
 * @date 2022-03-10 14:52
 */
@FunctionalInterface
public interface ChannelHandlerFunc {
    /**
     * 新建一个实例
     *
     * @return
     */
    ChannelHandler newInstance();
}
