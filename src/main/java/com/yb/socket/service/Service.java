package com.yb.socket.service;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 13:55
 */
public abstract class Service {
    /**
     * socket类型
     */
    protected SocketType socketType = SocketType.NORMAL;
    /**
     * 绑定端口，默认为8000
     */
    protected int port = 8000;
    /**
     * 多IP情况下绑定指定的IP(可以不设置)
     */
    protected String ip;
    /**
     * 是否启用keepAlive
     */
    protected boolean keepAlive = true;
    /**
     * 是否启用tcpNoDelay
     */
    protected boolean tcpNoDelay = true;
    /**
     * 工作线程池大小
     */
    protected int workerCount;

    /**
     * 是否开户业务处理线程池
     */
    protected boolean openExecutor = false;
    /**
     * 消息事件业务处理线程池
     */
    protected ExecutorService messageExecutor;
    /**
     * 通道事件业务处理线程池
     */
    protected ExecutorService channelExecutor;
    /**
     * 异常事件业务处理线程池
     */
    protected ExecutorService exceptionExecutor;
    /**
     * 消息事件业务处理线程池最小保持的线程数
     */
    protected int corePoolSize = 10;
    /**
     * 消息事件业务处理线程池最大线程数
     */
    protected int maximumPoolSize = 150;
    /**
     * 消息事件业务处理线程池队列最大值
     */
    protected int queueCapacity = 1000000;
    /**
     * 设置是否心跳检查
     */
    protected boolean checkHeartbeat = true;
    /**
     * 心跳检查时的读空闲时间
     */
    protected int readerIdleTimeSeconds = 0;
    /**
     * 心跳检查时的写空闲时间
     */
    protected int writerIdleTimeSeconds = 0;
    /**
     * 心跳检查时的读写空闲时间
     */
    protected int allIdleTimeSeconds = 90;

    protected IdleStateHandler timeoutHandler;
    protected ChannelInboundHandlerAdapter heartbeatHandler;

    protected LinkedHashMap<String, ChannelHandler> handlers = new LinkedHashMap<>();
    protected List<EventListener> eventListeners = new ArrayList<>();

    protected EventDispatcher eventDispatcher;

    public Service() {
        // 默认工作线程数
        this.workerCount = Runtime.getRuntime().availableProcessors() + 1;
        //添加JVM关闭时的勾子
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
    }

    protected void init() {
        if (openExecutor) {
            messageExecutor = new ThreadPoolExecutor(
                    this.corePoolSize,
                    this.maximumPoolSize,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(this.queueCapacity),
                    new BasicThreadFactory.Builder().namingPattern("MessageEventProcessor-%d").daemon(true).build(),
                    new ThreadPoolExecutor.AbortPolicy());

            exceptionExecutor = Executors.newCachedThreadPool(
                    new BasicThreadFactory.Builder().namingPattern("ExceptionEventProcessor-%d").daemon(true).build());

            channelExecutor = Executors.newCachedThreadPool(
                    new BasicThreadFactory.Builder().namingPattern("ChannelEventProcessor-%d").daemon(true).build());
        }
    }

    /**
     * shutdown
     */
    public abstract void shutdown();

    public void setExecutor(ExecutorService executor) {
        if (executor == null) {
            throw new NullPointerException("executor is null.");
        }
        ExecutorService preExecutor = this.messageExecutor;
        this.messageExecutor = executor;

        List<Runnable> tasks = preExecutor.shutdownNow();
        if (tasks != null && tasks.size() > 0) {
            for (Runnable task : tasks) {
                this.messageExecutor.execute(task);
            }
        }
    }

    public int getExecutorActiveCount() {
        if (messageExecutor instanceof ThreadPoolExecutor) {
            return ((ThreadPoolExecutor) messageExecutor).getActiveCount();
        }
        return -1;
    }

    public long getExecutorCompletedTaskCount() {
        if (messageExecutor instanceof ThreadPoolExecutor) {
            return ((ThreadPoolExecutor) messageExecutor).getCompletedTaskCount();
        }
        return -1;
    }

    public int getExecutorLargestPoolSize() {
        if (messageExecutor instanceof ThreadPoolExecutor) {
            return ((ThreadPoolExecutor) messageExecutor).getLargestPoolSize();
        }
        return -1;
    }

    public int getExecutorPoolSize() {
        if (messageExecutor instanceof ThreadPoolExecutor) {
            return ((ThreadPoolExecutor) messageExecutor).getPoolSize();
        }
        return -1;
    }

    public long getExecutorTaskCount() {
        if (messageExecutor instanceof ThreadPoolExecutor) {
            return ((ThreadPoolExecutor) messageExecutor).getTaskCount();
        }
        return -1;
    }

    public int getExecutorQueueSize() {
        if (messageExecutor instanceof ThreadPoolExecutor) {
            return ((ThreadPoolExecutor) messageExecutor).getQueue().size();
        }
        return -1;
    }

    public void addEventListener(EventListener listener) {
        this.eventListeners.add(listener);
    }

    public void addChannelHandler(String key, ChannelHandler handler) {
        this.handlers.put(key, handler);
    }

    public LinkedHashMap<String, ChannelHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(LinkedHashMap<String, ChannelHandler> handlers) {
        this.handlers = handlers;
    }

    public List<EventListener> getEventListeners() {
        return eventListeners;
    }

    public void setListeners(List<EventListener> listeners) {
        if (listeners == null) {
            listeners = new ArrayList<EventListener>();
        }
        eventListeners = listeners;
    }

    public SocketType getSocketType() {
        return socketType;
    }

    public void setSocketType(SocketType socketType) {
        this.socketType = socketType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public boolean isOpenExecutor() {
        return openExecutor;
    }

    public void setOpenExecutor(boolean openExecutor) {
        this.openExecutor = openExecutor;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public boolean isCheckHeartbeat() {
        return checkHeartbeat;
    }

    public void setCheckHeartbeat(boolean checkHeartbeat) {
        this.checkHeartbeat = checkHeartbeat;
    }

    public int getReaderIdleTimeSeconds() {
        return readerIdleTimeSeconds;
    }

    public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
        this.readerIdleTimeSeconds = readerIdleTimeSeconds;
    }

    public int getWriterIdleTimeSeconds() {
        return writerIdleTimeSeconds;
    }

    public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
        this.writerIdleTimeSeconds = writerIdleTimeSeconds;
    }

    public int getAllIdleTimeSeconds() {
        return allIdleTimeSeconds;
    }

    public void setAllIdleTimeSeconds(int allIdleTimeSeconds) {
        this.allIdleTimeSeconds = allIdleTimeSeconds;
    }

    class ShutdownHook extends Thread {
        private Service service;

        public ShutdownHook(Service service) {
            this.service = service;
        }

        @Override
        public void run() {
            service.shutdown();
        }
    }
}
