package com.yb.socket.push.server;

import com.yb.socket.listener.DefaultMqttMessageEventListener;
import com.yb.socket.service.WrappedChannel;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author daoshenzzg@163.com
 * @date 2019/2/26 14:27
 */
public class TestMqttMessageEventListener extends DefaultMqttMessageEventListener {
    private static final Logger logger = LoggerFactory.getLogger(TestMqttMessageEventListener.class);

    @Override
    public void connect(WrappedChannel channel, MqttConnectMessage msg) {
        String clientId = msg.payload().clientIdentifier();
        channel.attr(AttributeKey.valueOf("clientId")).set(clientId);
        super.connect(channel, msg);
    }

    @Override
    public void disConnect(WrappedChannel channel, MqttMessage msg) {
        ApplicationContext context = ServerContext.getContext().getApp();
        SubscribeServer subscribeServer = (SubscribeServer) context.getBean("subscribeServer");
        String channelId = channel.id().asShortText();
        try {
            subscribeServer.unSubscribe(channelId);
            logger.debug("取消订阅全部主题成功. channelId={}", channelId);
        } catch (Exception ex) {
            logger.error("取消订阅失败.", ex);
        }

        super.disConnect(channel, msg);
    }

    @Override
    public void subscribe(WrappedChannel channel, MqttSubscribeMessage msg) {
        ApplicationContext context = ServerContext.getContext().getApp();
        SubscribeServer subscribeServer = (SubscribeServer) context.getBean("subscribeServer");

        List<MqttTopicSubscription> topicSubscriptions = msg.payload().topicSubscriptions();
        String clientId = (String) channel.getChannel().attr(AttributeKey.valueOf("clientId")).get();
        String channelId = channel.id().asShortText();
        topicSubscriptions.forEach(topicSubscription -> {
            String topic = topicSubscription.topicName();
            MqttQoS mqttQoS = topicSubscription.qualityOfService();
            logger.debug("开始订阅. clientId={}, topic={}, qos={}", clientId, topic, mqttQoS.value());
            try {
                subscribeServer.subscribe(clientId, channelId, topic);
                logger.debug("订阅主题成功. clientId={}, channelId={}, topic={}", channelId, channelId, topic);
            } catch (Exception ex) {
                logger.error("订阅失败.", ex);
            }
        });
        super.subscribe(channel, msg);
    }

    @Override
    public void unSubscribe(WrappedChannel channel, MqttUnsubscribeMessage msg) {
        ApplicationContext context = ServerContext.getContext().getApp();
        SubscribeServer subscribeServer = (SubscribeServer) context.getBean("subscribeServer");
        String clientId = (String) channel.getChannel().attr(AttributeKey.valueOf("clientId")).get();
        List<String> topics = msg.payload().topics();
        topics.forEach(topic -> {
            try {
                subscribeServer.unSubscribe(clientId, topic);
                logger.debug("取消订阅主题成功. clientId={}, topic={}", clientId, topic);
            } catch (Exception ex) {
                logger.error("取消订阅失败.", ex);
            }
        });

        super.unSubscribe(channel, msg);
    }

    @Override
    public void publish(WrappedChannel channel, MqttPublishMessage msg) {
        String topic = msg.variableHeader().topicName();
        ByteBuf buf = msg.content().duplicate();
        byte[] tmp = new byte[buf.readableBytes()];
        buf.readBytes(tmp);
        String content = new String(tmp);
        String clientId = (String) channel.getChannel().attr(AttributeKey.valueOf("clientId")).get();
        logger.info("channelId={}, topic={}, message={}", clientId, topic, content);
    }
}
