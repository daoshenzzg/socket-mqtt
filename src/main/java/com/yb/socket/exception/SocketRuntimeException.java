package com.yb.socket.exception;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 15:08
 */
public class SocketRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SocketRuntimeException() {
        super();
    }

    public SocketRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketRuntimeException(String message) {
        super(message);
    }

    public SocketRuntimeException(Throwable cause) {
        super(cause);
    }

}
