package com.yb.socket.pojo;

import java.io.Serializable;

/**
 * 请求消息
 *
 * @author daoshenzzg@163.com
 * @date 2018/12/30 15:20
 */
public class Request extends BaseMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request [sequence=");
        sb.append(sequence);
        sb.append(", message=");
        sb.append(message);
        sb.append("]");
        return sb.toString();
    }
}
