package com.yb.socket.listener;

import com.yb.socket.future.InvokeFuture;
import com.yb.socket.pojo.Request;
import com.yb.socket.pojo.Response;
import com.yb.socket.service.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:18
 */
public class DefaultMessageEventListener implements MessageEventListener {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageEventListener.class);

    @Override
    public EventBehavior channelRead(ChannelHandlerContext ctx, WrappedChannel channel, Object msg) {
        if (logger.isDebugEnabled()) {
            logger.debug("Message received on channel '{}'.", channel.id().asShortText());
        }

        if (msg != null) {
            if (msg instanceof Request) {
                Request request = (Request) msg;
                if (request.getMessage() != null) {
                    // 处理请求消息
                    processRequest(ctx, channel, request);
                }
            } else if (msg instanceof Response) {
                Response response = (Response) msg;
                // 处理反馈消息
                processResponse(ctx, response, channel);
            }
        }
        return EventBehavior.CONTINUE;
    }

    private void processRequest(ChannelHandlerContext ctx, WrappedChannel channel, Request request) {
    }

    private void processResponse(ChannelHandlerContext ctx, Response response, WrappedChannel channel) {
        // Future方式
        InvokeFuture future = channel.getFutures().remove(response.getSequence());
        if (future != null) {
            if (response.getCause() != null) {
                future.setCause(response.getCause());
            } else {
                future.setResult(response);
            }
        }
    }
}
