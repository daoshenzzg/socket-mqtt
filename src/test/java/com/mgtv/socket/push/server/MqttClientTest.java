package com.mgtv.socket.push.server;

import com.mgtv.socket.service.mqtt.MqttServerTest;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/14 17:52
 */
public class MqttClientTest {

    private static final Logger logger = LoggerFactory.getLogger(MqttServerTest.class);

    public static void main(String[] args) {
        final String broker = "tcp://127.0.0.1:8000";
        final String clientId = "123456";
        final String topic = "/mgtv/notice";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            final MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            logger.info("Connecting to broker: {}", broker);
            connOpts.setServerURIs(new String[]{broker});
            connOpts.setUserName("admin");
            connOpts.setPassword("123456".toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(90);
            connOpts.setAutomaticReconnect(true);
            connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
            sampleClient.setCallback(new MqttCallbackExtended() {
                public void connectComplete(boolean reconnect, String serverURI) {
                    logger.info("connect success");

                    // 连接成功，需要上传客户端所有的订阅关系
                    try {
                        sampleClient.subscribe(topic, 0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                public void connectionLost(Throwable throwable) {
                    logger.error("mqtt connection lost.", throwable);
                }

                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    logger.info("message arrived. topic={}, message={}.", topic, new String(mqttMessage.getPayload()));
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    logger.info("delivery complete. messageId={}.", iMqttDeliveryToken.getMessageId());
                }
            });
            sampleClient.connect(connOpts);
        } catch (Exception me) {
            me.printStackTrace();
        }
    }
}
