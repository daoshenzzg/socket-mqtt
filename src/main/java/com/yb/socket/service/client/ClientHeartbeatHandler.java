package com.yb.socket.service.client;

import com.yb.socket.pojo.Heartbeat;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:33
 */
@ChannelHandler.Sharable
public class ClientHeartbeatHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ClientHeartbeatHandler.class);

    public ClientHeartbeatHandler() {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
                Channel channel = ctx.channel();
                if (channel != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("WRITER_IDLE, send Heartbeat...");
                    }
                    channel.writeAndFlush(Heartbeat.getSingleton());
                }
            } else if (e.state() == IdleState.READER_IDLE) {
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}