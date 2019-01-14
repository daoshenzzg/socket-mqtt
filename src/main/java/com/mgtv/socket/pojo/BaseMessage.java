package com.mgtv.socket.pojo;

import java.io.Serializable;

/**
 * @author zhiguang@mgtv.com
 * @date 2018/12/30 15:18
 */
public class BaseMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int sequence;

    public int getSequence() {
        return sequence;
    }
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
