package com.yb.socket.service.protocol;

import com.yb.socket.listener.EventBehavior;
import com.yb.socket.listener.MessageEventListener;
import com.yb.socket.pojo.protocol.ProtocolRequest;
import com.yb.socket.pojo.protocol.ProtocolResponse;
import com.yb.socket.service.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daoshenzzg@163.com
 * @date 2022-04-29 15:53
 */
public class ProtocolEchoMessageEventListener implements MessageEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolEchoMessageEventListener.class);

    @Override
    public EventBehavior channelRead(ChannelHandlerContext ctx, WrappedChannel channel, Object msg) {
        if (msg instanceof ProtocolRequest) {
            ProtocolRequest request = (ProtocolRequest) msg;
            ProtocolResponse response = new ProtocolResponse(request);

            Map<String, Object> body = new HashMap<>();
            @SuppressWarnings("unchecked")
            Map<String, String> data = (Map<String, String>) request.getBody().get("data");
            data.put("message", data.get("message").toUpperCase());
            body.put("data", data);
            response.setBody(body);

            channel.writeAndFlush(response);
            logger.info("send response '{}' success.", response);
        }

        return EventBehavior.CONTINUE;
    }
}
