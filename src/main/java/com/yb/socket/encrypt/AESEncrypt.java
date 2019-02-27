package com.yb.socket.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/14 10:46
 */
public class AESEncrypt extends BaseEncrypt implements Encrypt {
    private static final String CIPHER_TRIPLE_AES = "AES/ECB/PKCS5Padding";
    private static final byte[] salt = { (byte) 0xA4, (byte) 0x0B, (byte) 0xC8, (byte) 0x34, (byte) 0xD6, (byte) 0x95, (byte) 0xF3, (byte) 0x13 };
    private SecretKey secret = null;

    public AESEncrypt(String password, int length) throws Exception{
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 5, length);
        SecretKey tmp = factory.generateSecret(spec);
        secret = new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    @Override
    public byte[] encrypt(byte[] src) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRIPLE_AES);
        cipher.init(Cipher.ENCRYPT_MODE, secret);

        return cipher.doFinal(src);
    }

    @Override
    public byte[] decrypt(byte[] src) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRIPLE_AES);
        cipher.init(Cipher.DECRYPT_MODE, secret);
        return cipher.doFinal(src);
    }

    public static void main(String[] args) throws Exception {
        String testStr = "acd721232a191c87bfe3425d3e0f2b38";
        AESEncrypt aesEncrypt = new AESEncrypt("test_key", 128);
        String enStr = aesEncrypt.encrypt(testStr, "UTF-8");
        String deStr = aesEncrypt.decrypt(enStr, "UTF-8");

        System.out.println("加密前：" + testStr);
        System.out.println("加密后：" + enStr);
        System.out.println("解密后：" + deStr);
    }
}
