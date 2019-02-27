package com.yb.socket.pojo;

import java.io.Serializable;

/**
 * 响应消息
 *
 * @author daoshenzzg@163.com
 * @date 2018/12/30 15:21
 */
public class Response extends BaseMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int EXCEPTION = -1;

    public static final int SUCCESS = 0;

    private int code;
    private Object result;
    private Throwable cause;

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Response [sequence=");
        sb.append(sequence);
        sb.append(", code=");
        sb.append(code);
        sb.append(", result=");
        sb.append(result);
        sb.append("]");
        return sb.toString();
    }
}