package com.mgtv.socket.service.normal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mgtv.socket.listener.EventBehavior;
import com.mgtv.socket.listener.MessageEventListener;
import com.mgtv.socket.pojo.Request;
import com.mgtv.socket.pojo.Response;
import com.mgtv.socket.service.WrappedChannel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/3 21:45
 */
public class JsonEchoMessageEventListener implements MessageEventListener {
    @Override
    public EventBehavior channelRead(ChannelHandlerContext ctx, WrappedChannel channel, Object msg) {

        if (msg instanceof Request) {
            Request request = (Request) msg;
            if (request.getMessage() != null) {
                Response response = new Response();
                response.setSequence(request.getSequence());
                response.setCode(0);
                JSONObject data = JSON.parseObject(request.getMessage().toString());
                response.setResult(data.getString("message").toUpperCase());

                channel.writeAndFlush(response);
            }
        }

        return EventBehavior.CONTINUE;
    }
}
