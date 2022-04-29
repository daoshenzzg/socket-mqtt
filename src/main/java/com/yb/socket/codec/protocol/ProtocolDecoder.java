package com.yb.socket.codec.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义解码器粘包拆包处理
 *
 * @author daoshenzzg@163.com
 * @date 2022-04-29 15:46
 */
public class ProtocolDecoder extends ByteToMessageDecoder {

    private static final int HEADER_SIZE = 20;
    private static final int BODY_LENGTH_OFFSET = 2;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in == null || in.readableBytes() < HEADER_SIZE) {
            return;
        }
        in.markReaderIndex();

        ByteBuf headerBuf = Unpooled.buffer(HEADER_SIZE);
        in.readBytes(headerBuf);

        int bodyLength = headerBuf.getInt(BODY_LENGTH_OFFSET);
        if (in.readableBytes() < bodyLength) {
            in.resetReaderIndex();
            return;
        }
        in.resetReaderIndex();

        out.add(Protocol.decode(in));
    }

}
