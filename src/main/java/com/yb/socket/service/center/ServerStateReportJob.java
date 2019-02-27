package com.yb.socket.service.center;

import com.alibaba.fastjson.JSONObject;
import com.yb.socket.pojo.Request;
import com.yb.socket.service.client.Client;
import com.yb.socket.service.server.Server;
import com.yb.socket.util.AddressUtil;
import com.yb.socket.util.Sequence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:51
 */
public class ServerStateReportJob implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ServerStateReportJob.class);

    private static final long DEFAULT_PERIOD_IN_MILLIS = 5000L;

    private long periodInMillis = DEFAULT_PERIOD_IN_MILLIS;


    private Server server;
    private Client client;

    public ServerStateReportJob(Server server, Client client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (server.hasRegisteredToCenter()) {
                    if (!channelIsValid(client)) {
                        logger.warn("Server '{}' currently has no valid channel to register center '{}' to report " + "state, just try later.",
                                server.getServiceName(), client.getCurServer());
                    } else {
                        // 向center汇报当前server状态。
                        Request request = new Request();
                        request.setSequence(Sequence.getInstance().addAndGet("CENTER_CLIENT"));

                        JSONObject json = new JSONObject();
                        json.put("action", "updateConnects");
                        json.put("service", server.getServiceName());
                        json.put("ip", StringUtils.isEmpty(server.getIp()) ? AddressUtil.getLocalIp() : server.getIp());
                        json.put("port", server.getPort());
                        json.put("connects", server.getChannels().size());
                        request.setMessage(json.toString());

                        client.send(request);

                        logger.info("Server '{}' reported state to register center '{}' using sequence '{}'.",
                                new Object[] { server.getServiceName(), client.getCurServer(), request.getSequence() });
                    }
                } else {
                    logger.warn("Server '{}' has not registered to center, no need to report server state now, just " + "try later.", server.getServiceName());
                }
            } catch (Throwable ex) {
                logger.error("Server '{}' failed to report server state to register center.", server.getServiceName(), ex);
            }

            try {
                Thread.sleep(periodInMillis);
            } catch (InterruptedException ex) {
                logger.warn("Server state report job received InterruptedException when sleeping.");
            }
        }
    }

    private boolean channelIsValid(Client client) {
        if (client == null) {
            return false;
        }

        if (client.getChannel() == null || !client.getChannel().isActive()) {
            return false;
        }

        return true;
    }

}