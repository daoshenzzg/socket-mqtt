package com.mgtv.socket.push.server;

import com.mgtv.socket.listener.DefaultMqttMessageEventListener;
import com.mgtv.socket.service.WrappedChannel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/14 17:45
 */
public class MqttMessageEventListener extends DefaultMqttMessageEventListener {

    @Override
    public void connect(WrappedChannel channel, MqttConnectMessage msg) {
        super.connect(channel, msg);

        String channelId = channel.id().asShortText();
        String uuid = msg.payload().clientIdentifier();
        ChannelDao channelDao = (ChannelDao)AppContext.getInstance().getContext().getBean("channelDao");
        channelDao.connect(channelId, uuid);

    }

    @Override
    public void disConnect(WrappedChannel channel, MqttMessage msg) {
        super.disConnect(channel, msg);

        String channelId = channel.id().asShortText();
        ChannelDao channelDao = (ChannelDao)AppContext.getInstance().getContext().getBean("channelDao");
        channelDao.disConnect(channelId);
    }
}
