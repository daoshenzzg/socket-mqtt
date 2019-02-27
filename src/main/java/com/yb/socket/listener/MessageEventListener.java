package com.yb.socket.listener;

import com.yb.socket.service.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;

import java.util.EventListener;

/**
 * 消息监听器
 *
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:15
 */
public interface MessageEventListener extends EventListener {
    /**
     * 接收消息
     *
     * @param ctx
     * @param channel
     * @param msg
     * @return
     */
    EventBehavior channelRead(ChannelHandlerContext ctx, WrappedChannel channel, Object msg);
}
