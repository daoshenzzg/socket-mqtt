package com.mgtv.socket.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mgtv.socket.pojo.Heartbeat;
import com.mgtv.socket.pojo.Request;
import com.mgtv.socket.pojo.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Json 解码器
 *
 * @author zhiguang@mgtv.com
 * @date 2018/12/30 16:55
 */
@ChannelHandler.Sharable
public class JsonDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final String REQUEST = "request";
    private static final String RESPONSE = "response";
    private static final String HEARTBEAT = "heartbeat";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        byte[] tmp = new byte[buf.readableBytes()];
        buf.readBytes(tmp);
        String jsonStr = new String(tmp);

        JSONObject json = JSON.parseObject(jsonStr);
        String type = json.getString("type");
        if (type.equalsIgnoreCase(REQUEST)) {
            Request request = new Request();
            request.setSequence(json.getIntValue("sequence"));
            request.setMessage(json.getString("message"));
            out.add(request);
        } else if (type.equalsIgnoreCase(RESPONSE)) {
            Response response = new Response();
            response.setSequence(json.getIntValue("sequence"));
            response.setCode(json.getIntValue("code"));
            response.setResult(json.get("result"));
            out.add(response);
        } else if (type.equalsIgnoreCase(HEARTBEAT)) {
            out.add(Heartbeat.getSingleton());
        } else {
            out.add(buf);
        }
    }
}