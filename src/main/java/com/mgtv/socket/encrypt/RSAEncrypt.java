package com.mgtv.socket.encrypt;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author zhiguang@mgtv.com
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
        byte[] bytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pri_keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.privateKey = kf.generatePrivate(pri_keySpec);

        bytes = Base64.decode(publicKey);
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
        System.out.println(Base64.encodeBytes(keyPair.getPublic().getEncoded()));
        // 获取私钥密文
        System.out.println(Base64.encodeBytes(keyPair.getPrivate().getEncoded()));

        RSAEncrypt rsaEncrypt = new RSAEncrypt(keyPair);
        String enStr = rsaEncrypt.encrypt(testStr, "UTF-8");
        String deStr = rsaEncrypt.decrypt(enStr, "UTF-8");
        System.out.println("加密前：" + testStr);
        System.out.println("加密后：" + enStr);
        System.out.println("解密后：" + deStr);

        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIoEYdgivxdsBorLODoySsXD8RyJA2aVLrgjO7eOkJrXUsIR4+a0aA0TgdmHiQOpBQuDL8rxvv2B8Q7bAZWdSbYCCiebjXk2hVtLRFycpXzNv0/IqGpjrlT68bZAJ5CbpFQ/08f1x5DdVgAGcvjzqOP7iilBL9nciiScqVt/EWjHAgMBAAECgYB1XTifBR0em7wMdrd/tZikB/WW4GRF7YJnD38OnmsVYRl88p2sZ6k7xWTcSEcZF7e0jIAtwdk51i/ZBB+azMAKehiiL6La4CEeDxKSKhs0YDBA1yEVbKadFhyHk/rNy/djua+2sHMFYyTlo8lGyKihzddqZ7eyqOC3IkAqv/GDoQJBAP2iXlbNMQ6AwYTEzcQH3QF8S0thEu3aAcv5T8p40Ci1sbPGbXOYET27q+ANxDJpP3gEfXXi2eHuZJ/C0tCV/lkCQQCLTfECzQpb6DPMEBNNrBBvZ4NsAdaaPOdLoHZVL5fpZSHbkc2RU8ndM47Abp7LFN/S5xmFMXInezfbf87JyPwfAkEAhPvvLt/jStFjpfNyV8gvrqm26Mz7Gc7mhkYv+d8idVXe2H2/wY4H7DBMS+ur5Sqd5pWkGn1Y9EcEZ2fFFSyv8QJAMYlo4A8b1Ozwpms9Agzi10rfECRjNPvdYCZSjh5bjfKZpKPnjvtVuGRiKgnsS9lDcpMdnyCjMGj/xv1fAqCHDwJAbY1Sw5K2F0Nmt2C9NjTZzhq61NgEVINtGA5r9ziZ1M237+8zo6wTWo4HsiCHbNOz091GoFxOxp7Di5e6OAxBGA==";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGqAPhRl42MLbqCQ/DGmdTyBg1ItNOs+oJyZxmhSHiGX/mqK12qc7Ft4WXdVf8848H7AxEFZvHVjEpgsspdkZDqha14ymaZjXVVYk1oCPgieQ8EbKYrTg6iswhPP3E2SbZYFASwtTduqOVitRT7qrqCKlMwVnLdcGs2EyrlXtznwIDAQAB";
        rsaEncrypt = new RSAEncrypt(privateKey, publicKey);
        deStr = rsaEncrypt.decrypt(enStr, "UTF-8");
        System.out.println("加密前：" + testStr);
        System.out.println("加密后：" + enStr);
        System.out.println("解密后：" + deStr);
    }
}
