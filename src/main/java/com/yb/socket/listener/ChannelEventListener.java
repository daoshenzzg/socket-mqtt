package com.yb.socket.listener;

import com.yb.socket.service.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;

import java.util.EventListener;

/**
 * 通道事件监听器
 *
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:21
 */
public interface ChannelEventListener extends EventListener {

    /**
     * 通道连接
     *
     * @param ctx
     * @param channel
     * @return
     */
    EventBehavior channelActive(ChannelHandlerContext ctx, WrappedChannel channel);

    /**
     * 通道关闭
     *
     * @param ctx
     * @param channel
     * @return
     */
    EventBehavior channelInactive(ChannelHandlerContext ctx, WrappedChannel channel);
}
