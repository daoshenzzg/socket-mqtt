package com.yb.socket.status;

import com.yb.socket.service.server.Server;
import com.yb.socket.service.server.ServerDispatchHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 18:23
 */
public class StatusServer extends Server {
    private static final Logger logger = LoggerFactory.getLogger(StatusServer.class);

    public StatusServer() {
    }

    @Override
    protected void init() {
        checkHeartbeat = false;
        openExecutor = false;
        openCount = false;
        super.init();


        this.addChannelHandler("framer", new DelimiterBasedFrameDecoder(1024, Delimiters.lineDelimiter()));
        this.addChannelHandler("decoder", new StringDecoder());
        this.addChannelHandler("encoder", new StringEncoder());
        this.addEventListener(new StatusMessageEventListener());
    }

    @Override
    public ChannelFuture bind() {
        init();

        bootstrap = new ServerBootstrap();
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, keepAlive);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, tcpNoDelay);
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                // 注册各种自定义Handler
                LinkedHashMap<String, ChannelHandler> handlers = getHandlers();
                for (String key : handlers.keySet()) {
                    pipeline.addLast(key, handlers.get(key));
                }
                //注册事件分发Handler
                ServerDispatchHandler dispatchHandler = new ServerDispatchHandler(eventDispatcher);
                pipeline.addLast("dispatchHandler", dispatchHandler);
            }
        });

        // 监听端口
        InetSocketAddress socketAddress = null;
        if (StringUtils.isBlank(ip)) {
            socketAddress = new InetSocketAddress(port);
        } else {
            socketAddress = new InetSocketAddress(ip, port);
        }

        ChannelFuture future = bootstrap.bind(socketAddress);

        InetSocketAddress sockAddr = socketAddress;
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture ch) throws Exception {
                ch.await();
                if (ch.isSuccess()) {
                    logger.info("Status Server started, listening on '{}.", sockAddr);
                } else {
                    logger.error("Failed to start status server '{}', caused by: '{}'.", sockAddr, ch.cause());
                }
            }
        });

        return future;
    }
}
