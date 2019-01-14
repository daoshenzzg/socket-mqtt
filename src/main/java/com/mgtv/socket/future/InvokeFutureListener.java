package com.mgtv.socket.future;

/**
 * InvokeFuture监听器
 *
 * @author zhiguang@mgtv.com
 * @date 2018/12/30 15:05
 */
public interface InvokeFutureListener {
    /**
     * 完成操作
     *
     * @param future
     * @throws Exception
     */
    void operationComplete(InvokeFuture future) throws Exception;
}
