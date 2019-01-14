package com.mgtv.socket.compression;

import java.io.IOException;

/**
 * @author zhiguang@mgtv.com
 * @date 2019/1/14 11:09
 */
public interface Compression {

    /**
     * compress
     *
     * @param buffer
     * @return
     * @throws IOException
     */
    byte[] compress(byte[] buffer) throws IOException;

    /**
     * decompress
     *
     * @param buffer
     * @return
     * @throws IOException
     */
    byte[] decompress(byte[] buffer) throws IOException;
}
