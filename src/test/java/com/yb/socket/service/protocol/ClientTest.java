package com.yb.socket.service.protocol;

import com.yb.socket.codec.protocol.ProtocolDecoder;
import com.yb.socket.codec.protocol.ProtocolEncoder;
import com.yb.socket.pojo.protocol.ProtocolRequest;
import com.yb.socket.pojo.protocol.ProtocolResponse;
import com.yb.socket.service.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daoshenzzg@163.com
 * @date 2022-04-29 15:55
 */
public class ClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.setIp("127.0.0.1");
        client.setPort(8000);
        client.setConnectTimeout(10000);
        client.setCheckHeartbeat(true);

        client.addChannelHandler("decoder", ProtocolEncoder::new);
        client.addChannelHandler("encoder", ProtocolDecoder::new);
        logger.info("Start to connect: {}.", System.currentTimeMillis());

        client.connect();
        logger.info("Connection completed: {}.", System.currentTimeMillis());

        ProtocolRequest request = new ProtocolRequest();
        request.setVersion((byte) 1);
        request.setSequence(123456);
        Map<String, Object> body = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        data.put("message", "hello world");
        body.put("data", data);
        request.setBody(body);
        ProtocolResponse response = (ProtocolResponse) client.sendWithSync(request, 3000);
        logger.info("成功接收到同步的返回: '{}'.", response);

        Thread.sleep(60 * 1000L);

        client.shutdown();
    }
}
