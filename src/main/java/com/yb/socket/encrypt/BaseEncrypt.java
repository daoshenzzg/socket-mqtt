package com.yb.socket.encrypt;

import java.util.Base64;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/14 10:43
 */
public abstract class BaseEncrypt implements Encrypt {

    @Override
    public String encrypt(String src, String charset) throws Exception {
        byte[] srcBytes = src.getBytes(charset);
        byte[] enBytes = encrypt(srcBytes);
        return Base64.getEncoder().encodeToString(enBytes);
    }

    @Override
    public String decrypt(String src, String charset) throws Exception {
        byte[] srcBytes = Base64.getDecoder().decode(src);
        byte[] deBytes = decrypt(srcBytes);
        return new String(deBytes, charset);
    }
}
