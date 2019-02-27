package com.yb.socket.service.client;

import com.yb.socket.service.EventDispatcher;
import com.yb.socket.service.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:25
 */
public class ClientDispatchHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ClientDispatchHandler.class);

    private EventDispatcher eventDispatcher;

    public ClientDispatchHandler(EventDispatcher eventDispatcher) {
        if (eventDispatcher == null) {
            throw new IllegalArgumentException("eventDispatcher");
        }

        this.eventDispatcher = eventDispatcher;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Message received from channel '{}' : '{}'.", ctx.channel().id().asShortText(), msg.toString());
        }

        WrappedChannel channel = ((BaseClient) eventDispatcher.getService()).getChannel();
        eventDispatcher.dispatchMessageEvent(ctx, channel, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Connected on channel '{}'.", ctx.channel().id().asShortText());
        }

        WrappedChannel channel = ((BaseClient) eventDispatcher.getService()).getChannel();
        if (channel == null) {
            channel = new WrappedChannel(ctx.channel());
            ((BaseClient) eventDispatcher.getService()).setChannel(channel);
        }
        eventDispatcher.dispatchChannelEvent(ctx, channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        closeChannel(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        WrappedChannel channel = ((BaseClient) eventDispatcher.getService()).getChannel();
        if (channel != null) {
            eventDispatcher.dispatchExceptionCaught(ctx, channel, cause);
        }
        // 处理IOException，主动关闭channel
        if (cause instanceof IOException) {
            ctx.close();
            closeChannel(ctx);
        }
    }

    private void closeChannel(ChannelHandlerContext ctx) {
        WrappedChannel channel = ((BaseClient) eventDispatcher.getService()).getChannel();
        if (channel != null) {
            ((BaseClient) eventDispatcher.getService()).setChannel(null);
            if (logger.isDebugEnabled()) {
                logger.debug("Channel '{}' was closed.", channel.id().asShortText());
            }

            eventDispatcher.dispatchChannelEvent(ctx, channel);
        }
    }
}
