package com.mgtv.socket.service;

import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author zhiguang@mgtv.com
 * @date 2018/12/30 16:11
 */
@ChannelHandler.Sharable
public class SharableIdleStateHandler extends IdleStateHandler {
    public SharableIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
    }
}
