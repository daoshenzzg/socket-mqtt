package com.yb.socket.service.client;

import com.yb.socket.exception.SocketRuntimeException;
import com.yb.socket.exception.SocketTimeoutException;
import com.yb.socket.listener.DefaultMessageEventListener;
import com.yb.socket.pojo.Request;
import com.yb.socket.pojo.Response;
import com.yb.socket.service.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 16:23
 */
public class BaseClient extends Service {
    private static final Logger logger = LoggerFactory.getLogger(BaseClient.class);

    /**
     * 建立连接超时时间(毫秒), 默认3000
     */
    protected int connectTimeout = 3000;
    /**
     * 同步调用默认超时时间(毫秒)
     */
    protected int syncInvokeTimeout = 3000;

    protected ClientDispatchHandler dispatchHandler;
    /**
     * 当前连接Server
     */
    protected SocketAddress curServer;

    protected WrappedChannel channel;

    protected Semaphore semaphore = new Semaphore(0);

    protected EventLoopGroup group;
    protected Bootstrap bootstrap;

    public BaseClient() {
        super();
    }

    @Override
    protected void init() {
        super.init();
        // 将一些handler放在这里初始化是为了防止多例的产生。
        if (checkHeartbeat) {
            timeoutHandler = new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
            heartbeatHandler = new ClientHeartbeatHandler();
        }

        eventDispatcher = new EventDispatcher(this);
        dispatchHandler = new ClientDispatchHandler(eventDispatcher);

        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        this.addEventListener(new DefaultMessageEventListener());
    }

    protected ChannelFuture doConnect(final SocketAddress socketAddress, boolean sync) {
        // 连接server
        curServer = socketAddress;
        try {
            ChannelFuture future = bootstrap.connect(socketAddress).sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture ch) throws Exception {
                    ch.await();
                    if (ch.isSuccess()) {
                        channel = new WrappedChannel(ch.channel());
                        if (logger.isDebugEnabled()) {
                            logger.debug("Connect to '{}' success.", socketAddress);
                        }
                    } else {
                        logger.error("Failed to connect to '{}', caused by: '{}'.", socketAddress, ch.cause());
                    }

                    semaphore.release(Integer.MAX_VALUE - semaphore.availablePermits());
                }
            });
            future.channel().closeFuture();

            if (sync) {
                Throwable cause = null;
                try {
                    if (!semaphore.tryAcquire(connectTimeout, TimeUnit.MILLISECONDS)) {
                        cause = new SocketTimeoutException("time out.");
                    }
                } catch (InterruptedException ex) {
                    throw new SocketTimeoutException(ex);
                }

                if (cause != null) {
                    throw new SocketRuntimeException(cause);
                }
            }
            return future;
        } catch (Exception ex) {
            throw new SocketRuntimeException(ex);
        }
    }

    public ChannelFuture connect(final SocketAddress socketAddress) {
        return connect(socketAddress, true);
    }

    public ChannelFuture connect(final SocketAddress socketAddress, boolean sync) {
        init();
        bootstrap.option(ChannelOption.SO_KEEPALIVE, keepAlive);
        bootstrap.option(ChannelOption.TCP_NODELAY, tcpNoDelay);
        bootstrap.group(group).channel(NioSocketChannel.class);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                // 注册各种自定义Handler
                LinkedHashMap<String, ChannelHandler> handlers = getHandlers();
                for (String key : handlers.keySet()) {
                    pipeline.addLast(key, handlers.get(key));
                }

                if (socketType.equals(SocketType.MQTT_WS)) {
                    pipeline.addFirst("httpRequestDecoder", new HttpRequestDecoder());
                    pipeline.addFirst("httpObjectAggregator", new HttpObjectAggregator(65536));
                    pipeline.addFirst("httpResponseEncoder", new HttpResponseEncoder());
                }

                if (checkHeartbeat) {
                    pipeline.addLast("timeout", timeoutHandler);
                    pipeline.addLast("idleHandler", heartbeatHandler);
                }

                // 注册事件分发Handler
                pipeline.addLast("dispatchHandler", dispatchHandler);
            }
        });

        return doConnect(socketAddress, sync);
    }

    @Override
    public void shutdown() {
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    public ChannelFuture send(Object message) {
        if (channel == null) {
            throw new SocketRuntimeException("channel have not connect.");
        }
        return channel.send(message);
    }

    public Response sendWithSync(Request message) {
        if (channel == null) {
            throw new SocketRuntimeException("channel have not connect.");
        }
        return channel.sendSync(message, syncInvokeTimeout);
    }

    public Response sendWithSync(Request message, int timeout) {
        if (channel == null) {
            throw new SocketRuntimeException("channel have not connect.");
        }
        return channel.sendSync(message, timeout);
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSyncInvokeTimeout() {
        return syncInvokeTimeout;
    }

    public void setSyncInvokeTimeout(int syncInvokeTimeout) {
        this.syncInvokeTimeout = syncInvokeTimeout;
    }

    public SocketAddress getCurServer() {
        return curServer;
    }

    public void setCurServer(SocketAddress curServer) {
        this.curServer = curServer;
    }

    public WrappedChannel getChannel() {
        return channel;
    }

    public void setChannel(WrappedChannel channel) {
        this.channel = channel;
    }
}
