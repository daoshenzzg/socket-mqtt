package com.yb.socket.status;

import com.yb.socket.listener.EventBehavior;
import com.yb.socket.listener.MessageEventListener;
import com.yb.socket.service.WrappedChannel;
import com.yb.socket.service.server.Server;
import com.yb.socket.service.server.ServerContext;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 18:24
 */
public class StatusMessageEventListener implements MessageEventListener {
    private static final String CMD_STATUS = "get status";
    private static final String CMD_CONFIG = "get config";
    private static final String CMD_QUIT = "quit";
    private static final String CMD_EXIT = "exit";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public EventBehavior channelRead(ChannelHandlerContext ctx, WrappedChannel channel, Object msg) {
        if (msg instanceof String) {
            String command = (String) msg;

            if (!StringUtils.isBlank(command)) {
                Map<String, Object> resultMap = null;
                if (command.equalsIgnoreCase(CMD_STATUS)) {
                    resultMap = doGetStatus();
                } else if (command.equalsIgnoreCase(CMD_CONFIG)) {
                    resultMap = doGetConfig();
                } else if (command.equalsIgnoreCase(CMD_QUIT) || command.equalsIgnoreCase(CMD_EXIT)) {
                    channel.close();
                    return EventBehavior.BREAK;
                } else {
                    resultMap = new LinkedHashMap<>();
                    resultMap.put("error", "unsupported command:" + command);
                }

                channel.writeAndFlush(formatResultMap(resultMap), false);
            }
        }
        return EventBehavior.CONTINUE;
    }

    private String formatResultMap(Map<String, Object> resultMap) {
        if (resultMap == null || resultMap.size() <= 0) {
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{").append(LINE_SEPARATOR);
        for (Map.Entry<String, Object> row : resultMap.entrySet()) {
            sb.append(String.format(" %-25s", row.getKey())).append(":\t").append(row.getValue()).append(LINE_SEPARATOR);
        }
        sb.append("}").append(LINE_SEPARATOR);

        return sb.toString();
    }

    private Map<String, Object> doGetStatus() {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        Server busServer = ServerContext.getContext().getServer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if (busServer != null) {
            //server名称
            resultMap.put("serviceName", busServer.getServiceName());
            //server启动时间
            resultMap.put("serverStartTime", sdf.format(new Date(busServer.getStartTime())));
            //当前连接数
            resultMap.put("clientNumber", busServer.getCountInfo().getCurChannelNum());
            //最大连接数
            resultMap.put("maxClientNumber", busServer.getCountInfo().getMaxChannelNum());
            //最后次接收消息时间
            if (busServer.getCountInfo().getLastReceive() > 0) {
                resultMap.put("lastReceiveTime", sdf.format(new Date(busServer.getCountInfo().getLastReceive())));
            } else {
                resultMap.put("lastReceiveTime", "no message received");
            }
            //最后次发送消息时间
            if (busServer.getCountInfo().getLastSent() > 0) {
                resultMap.put("lastSentTime", sdf.format(new Date(busServer.getCountInfo().getLastSent())));
            } else {
                resultMap.put("lastSentTime", "no message send");
            }
            //接收消息数
            resultMap.put("receiveNumber", busServer.getCountInfo().getReceiveNum().get());
            //发送消息数
            resultMap.put("sentNumber", busServer.getCountInfo().getSentNum().get());
            // 心跳次数
            resultMap.put("heartbeatNumber", busServer.getCountInfo().getHeartbeatNum().get());
        } else {
            resultMap.put("error", "business server is not found!");
        }

        return resultMap;
    }

    private Map<String, Object> doGetConfig() {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Server busServer = ServerContext.getContext().getServer();
        if (busServer != null) {
            //ip
            resultMap.put("ip", busServer.getIp() == null ? "not specified" : busServer.getIp());
            //port
            resultMap.put("port", busServer.getPort());
            //keepAlive
            resultMap.put("keepAlive", busServer.isKeepAlive());
            //tcpNoDelay
            resultMap.put("tcpNoDelay", busServer.isTcpNoDelay());
            //workerCount
            //resultMap.put("workerCount", busServer.getWorkerCount());
            //executorFlag
            resultMap.put("executorFlag", busServer.isOpenExecutor());
            if (busServer.isOpenExecutor()) {
                //corePoolSize
                resultMap.put("corePoolSize", busServer.getCorePoolSize());
                //maximumPoolSize
                resultMap.put("maximumPoolSize", busServer.getMaximumPoolSize());
                //queueCapacity
                resultMap.put("queueCapacity", busServer.getQueueCapacity());
            }

            //checkHeartbeat
            resultMap.put("checkHeartbeat", busServer.isCheckHeartbeat());
            if (busServer.isCheckHeartbeat()) {
                //readerIdleTimeSeconds
                resultMap.put("readerIdleTimeSeconds", busServer.getReaderIdleTimeSeconds());
                //writerIdleTimeSeconds
                resultMap.put("writerIdleTimeSeconds", busServer.getWriterIdleTimeSeconds());
                //allIdleTimeSeconds
                resultMap.put("allIdleTimeSeconds", busServer.getAllIdleTimeSeconds());
            }
        } else {
            resultMap.put("error", "business server is not found!");
        }

        return resultMap;
    }
}