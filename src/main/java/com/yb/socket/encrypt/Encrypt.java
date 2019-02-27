package com.yb.socket.encrypt;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/14 10:41
 */
public interface Encrypt {
    /**
     * encrypt
     *
     * @param src
     * @return
     * @throws Exception
     */
    byte[] encrypt(byte[] src) throws Exception;

    /**
     * decrypt
     *
     * @param src
     * @return
     * @throws Exception
     */
    byte[] decrypt(byte[] src) throws Exception;

    /**
     * encrypt
     *
     * @param src
     * @param charset
     * @return
     * @throws Exception
     */
    String encrypt(String src, String charset) throws Exception;

    /**
     * decrypt
     *
     * @param src
     * @param charset
     * @return
     * @throws Exception
     */
    String decrypt(String src, String charset) throws Exception;
}
