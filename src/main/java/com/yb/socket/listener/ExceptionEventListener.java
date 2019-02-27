package com.yb.socket.listener;

import com.yb.socket.listener.EventBehavior;
import com.yb.socket.service.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;

import java.util.EventListener;

/**
 * 异常监听器
 *
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:20
 */
public interface ExceptionEventListener extends EventListener {
    /**
     * 异常捕获
     *
     * @param ctx
     * @param channel
     * @param cause
     * @return
     */
    EventBehavior exceptionCaught(ChannelHandlerContext ctx, WrappedChannel channel, Throwable cause);
}