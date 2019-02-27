package com.yb.socket.exception;

/**
 * @author daoshenzzg@163.com
 * @date 2018/12/30 15:10
 */
public class SocketTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SocketTimeoutException() {
        super();
    }

    public SocketTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketTimeoutException(String message) {
        super(message);
    }

    public SocketTimeoutException(Throwable cause) {
        super(cause);
    }
}
