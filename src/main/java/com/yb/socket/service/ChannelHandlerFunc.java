package com.yb.socket.service;

import io.netty.channel.ChannelHandler;

/**
 * @author daoshenzzg@163.com
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
