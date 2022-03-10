package com.yb.socket.service.normal;

import com.alibaba.fastjson.JSONObject;
import com.yb.socket.codec.JsonDecoder;
import com.yb.socket.codec.JsonEncoder;
import com.yb.socket.pojo.Request;
import com.yb.socket.pojo.Response;
import com.yb.socket.service.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 18:43
 */
public class ClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);

    public static void main(String[] args) {

        Client client = new Client();
        client.setIp("127.0.0.1");
        client.setPort(8000);
        client.setConnectTimeout(10000);
        client.addChannelHandler("decoder", JsonDecoder::new);
        client.addChannelHandler("encoder", JsonEncoder::new);
        client.connect();

        for (int i = 0; i < 2; i++) {
            JSONObject message = new JSONObject();
            message.put("action", "echo");
            message.put("message", "hello world!");

            Request request = new Request();
            request.setSequence(i);
            request.setMessage(message);
            Response response = client.sendWithSync(request, 3000);

            logger.info("成功接收到同步的返回: '{}'.", response);
        }

        client.shutdown();
    }
}
