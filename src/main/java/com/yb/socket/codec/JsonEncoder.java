package com.yb.socket.codec;

import com.alibaba.fastjson.JSONObject;
import com.yb.socket.pojo.BaseMessage;
import com.yb.socket.pojo.Heartbeat;
import com.yb.socket.pojo.Request;
import com.yb.socket.pojo.Response;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Json 编码器
 *
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:56
 */
@ChannelHandler.Sharable
public class JsonEncoder extends MessageToMessageEncoder<BaseMessage> {
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";
    private static final String HEARTBEAT = "heartbeat";

    @Override
    protected void encode(ChannelHandlerContext ctx, BaseMessage msg, List<Object> out) throws Exception {
        if (msg instanceof Request) {
            Request request = (Request) msg;
            JSONObject json = new JSONObject();
            json.put("type", REQUEST);
            json.put("message", request.getMessage());
            json.put("sequence", request.getSequence());
            ByteBuf buf = Unpooled.copiedBuffer(json.toString().getBytes());
            out.add(buf);
        } else if (msg instanceof Response) {
            Response response = (Response) msg;
            JSONObject json = new JSONObject();
            json.put("type", RESPONSE);
            json.put("code", response.getCode());
            json.put("result", response.getResult());
            json.put("sequence", response.getSequence());
            ByteBuf buf = Unpooled.copiedBuffer(json.toString().getBytes());
            out.add(buf);
        } else if (msg instanceof Heartbeat) {
            JSONObject json = new JSONObject();
            json.put("type", HEARTBEAT);
            ByteBuf buf = Unpooled.copiedBuffer(json.toString().getBytes());
            out.add(buf);
        } else {
            throw new CodecException("unknown message type: " + msg);
        }
    }
}
