package com.yb.socket.encrypt;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author daoshenzzg@163.com
 * @date 2019/1/14 10:49
 */
public class RSAEncrypt extends BaseEncrypt implements Encrypt {

    private static final String CIPHER_TRIPLE_AES = "RSA/ECB/PKCS1Padding";
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAEncrypt(KeyPair keyPair) throws Exception {
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public RSAEncrypt(String privateKey, String publicKey)
            throws Exception {
        byte[] bytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pri_keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.privateKey = kf.generatePrivate(pri_keySpec);

        bytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec pub_keySpec = new X509EncodedKeySpec(bytes);
        this.publicKey = kf.generatePublic(pub_keySpec);
    }

    @Override
    public byte[] encrypt(byte[] src) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRIPLE_AES);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(src);
    }

    @Override
    public byte[] decrypt(byte[] src) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRIPLE_AES);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(src);
    }

    public static KeyPair createRSAKeyPair() throws NoSuchAlgorithmException {
        return createRSAKeyPair("RSA", 512);
    }

    public static KeyPair createRSAKeyPair(String algorithm, int length) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
        // 密钥位数
        keyPairGen.initialize(length);
        // 密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    public static void main(String[] args) throws Exception {
        String testStr = "hello world";

        KeyPair keyPair = RSAEncrypt.createRSAKeyPair();
        // 获取公钥密文
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        System.out.println(publicKey);
        // 获取私钥密文
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        System.out.println(privateKey);

        RSAEncrypt rsaEncrypt = new RSAEncrypt(keyPair);
        String enStr = rsaEncrypt.encrypt(testStr, "UTF-8");
        String deStr = rsaEncrypt.decrypt(enStr, "UTF-8");
        System.out.println("加密前：" + testStr);
        System.out.println("加密后：" + enStr);
        System.out.println("解密后：" + deStr);

        rsaEncrypt = new RSAEncrypt(privateKey, publicKey);
        deStr = rsaEncrypt.decrypt(enStr, "UTF-8");
        System.out.println("加密前：" + testStr);
        System.out.println("加密后：" + enStr);
        System.out.println("解密后：" + deStr);
    }
}
