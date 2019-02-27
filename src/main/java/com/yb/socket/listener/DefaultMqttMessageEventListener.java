package com.yb.socket.listener;

import com.yb.socket.service.WrappedChannel;
import com.yb.socket.service.server.Server;
import com.yb.socket.service.server.ServerContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/4 11:17
 */
public class DefaultMqttMessageEventListener implements MessageEventListener {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMqttMessageEventListener.class);

    @Override
    public EventBehavior channelRead(ChannelHandlerContext ctx, WrappedChannel channel, Object msg) {
        if (msg instanceof MqttMessage) {
            MqttMessage message = (MqttMessage) msg;
            MqttMessageType messageType = message.fixedHeader().messageType();
            switch (messageType) {
                case CONNECT:
                    this.connect(channel, (MqttConnectMessage) message);
                    break;
                case PUBLISH:
                    this.publish(channel, (MqttPublishMessage) message);
                    break;
                case SUBSCRIBE:
                    this.subscribe(channel, (MqttSubscribeMessage) message);
                    break;
                case UNSUBSCRIBE:
                    this.unSubscribe(channel, (MqttUnsubscribeMessage) message);
                    break;
                case PINGREQ:
                    this.pingReq(channel, message);
                    break;
                case DISCONNECT:
                    this.disConnect(channel, message);
                    break;
                default:
                    if (logger.isDebugEnabled()) {
                        logger.debug("Nonsupport server message  type of '{}'.", messageType);
                    }
                    break;
            }
        }
        return EventBehavior.CONTINUE;
    }

    public void connect(WrappedChannel channel, MqttConnectMessage msg) {
        if (logger.isDebugEnabled()) {
            String clientId = msg.payload().clientIdentifier();
            logger.debug("MQTT CONNECT received on channel '{}', clientId is '{}'.",
                    channel.id().asShortText(), clientId);
        }

        MqttConnAckMessage okResp = (MqttConnAckMessage) MqttMessageFactory.newMessage(new MqttFixedHeader(
                        MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, true), null);
        channel.writeAndFlush(okResp);
    }

    public void pingReq(WrappedChannel channel, MqttMessage msg) {
        if (logger.isDebugEnabled()) {
            logger.debug("MQTT pingReq received.");
        }

        Server server = ServerContext.getContext().getServer();
        if(server != null) {
            server.getCountInfo().getHeartbeatNum().incrementAndGet();
        }

        MqttMessage pingResp = new MqttMessage(new MqttFixedHeader(MqttMessageType.PINGRESP, false,
                MqttQoS.AT_MOST_ONCE, false, 0));
        channel.writeAndFlush(pingResp);
    }

    public void disConnect(WrappedChannel channel, MqttMessage msg) {
        if (channel.isActive()) {
            channel.close();

            if (logger.isDebugEnabled()) {
                logger.debug("MQTT channel '{}' was closed.", channel.id().asShortText());
            }
        }
    }

    public void publish(WrappedChannel channel, MqttPublishMessage msg) {
    }

    public void subscribe(WrappedChannel channel, MqttSubscribeMessage msg) {
        MqttSubAckMessage subAckMessage = (MqttSubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(msg.variableHeader().messageId()),
                new MqttSubAckPayload(0));
        channel.writeAndFlush(subAckMessage);
    }

    public void unSubscribe(WrappedChannel channel, MqttUnsubscribeMessage msg) {
        MqttUnsubAckMessage unSubAckMessage = (MqttUnsubAckMessage) MqttMessageFactory.newMessage(
                new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(msg.variableHeader().messageId()), null);
        channel.writeAndFlush(unSubAckMessage);
    }

}
