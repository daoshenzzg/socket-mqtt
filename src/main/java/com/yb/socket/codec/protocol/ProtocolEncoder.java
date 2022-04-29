package com.yb.socket.codec.protocol;

import com.yb.socket.pojo.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author daoshenzzg@163.com
 * @date 2022-04-29 15:46
 */
@ChannelHandler.Sharable
public class ProtocolEncoder extends MessageToByteEncoder<BaseMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, BaseMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(Protocol.encode(msg));
    }
}
