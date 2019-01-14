package com.mgtv.socket.listener;

import com.mgtv.socket.service.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhiguang@mgtv.com
 * @date 2018/12/30 16:19
 */
public class DefaultExceptionListener implements ExceptionEventListener {
    private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionListener.class);

    @Override
    public EventBehavior exceptionCaught(ChannelHandlerContext ctx, WrappedChannel channel, Throwable cause) {
        if (cause != null && channel.remoteAddress() != null) {
            logger.warn("Exception caught on channel {}, caused by: '{}'.", channel.id().asShortText(), cause);
        }

        return EventBehavior.CONTINUE;
    }
}
