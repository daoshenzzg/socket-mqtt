package com.yb.socket.count;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/11 16:55
 */
public class CountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 最后次接收消息
     */
    private long lastReceive;
    /**
     * 最后次接收消息
     */
    private long lastSent;
    /**
     * 最大连接数
     */
    private long maxChannelNum;
    /**
     * 当前连接数
     */
    private long curChannelNum;
    /**
     * 接收消息数
     */
    private AtomicLong receiveNum = new AtomicLong();
    /**
     * 发送消息数
     */
    private AtomicLong sentNum = new AtomicLong();
    /**
     * 收发心跳数
     */
    private AtomicLong heartbeatNum = new AtomicLong();

    public long getCurChannelNum() {
        return curChannelNum;
    }

    public void setCurChannelNum(long curChannelNum) {
        this.curChannelNum = curChannelNum;
        if (this.maxChannelNum < curChannelNum) {
            this.maxChannelNum = curChannelNum;
        }
    }

    public long getMaxChannelNum() {
        return maxChannelNum;
    }

    public AtomicLong getReceiveNum() {
        return receiveNum;
    }

    public AtomicLong getSentNum() {
        return sentNum;
    }

    public AtomicLong getHeartbeatNum() {
        return heartbeatNum;
    }

    public long getLastReceive() {
        return lastReceive;
    }

    public void setLastReceive(long lastReceive) {
        if (this.lastReceive < lastReceive) {
            this.lastReceive = lastReceive;
        }
    }

    public long getLastSent() {
        return lastSent;
    }

    public void setLastSent(long lastSent) {
        if (this.lastSent < lastSent) {
            this.lastSent = lastSent;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StatisticInfo [lastReceive=").append(this.lastReceive);
        sb.append(", lastSent=").append(this.lastSent);
        sb.append(", receiveNum=").append(this.receiveNum);
        sb.append(", sentNum=").append(this.sentNum);
        sb.append(", heartbeatNum=").append(this.heartbeatNum);
        sb.append(", maxChannelNum=").append(this.maxChannelNum);
        sb.append(", curChannelNum=").append(this.curChannelNum);
        sb.append("]");
        return sb.toString();
    }
}
