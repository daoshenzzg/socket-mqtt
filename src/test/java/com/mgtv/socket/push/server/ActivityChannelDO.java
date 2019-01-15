package com.mgtv.socket.push.server;

import java.sql.Date;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/14 17:28
 */
public class ActivityChannelDO {
    private String channelId;
    private String uuid;
    private Date createTime;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
