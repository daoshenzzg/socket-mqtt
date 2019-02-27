package com.yb.socket.push.mqtt;

/**
 * @author daoshenzzg@163.com
 * @date 2019/2/26 14:58
 */
public class ChannelDO {

    public String channelId;
    public String clientId;
    public String topic;
    public int createTime;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }
}
