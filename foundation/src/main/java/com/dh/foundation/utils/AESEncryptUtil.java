package com.dh.foundation.utils;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 * Created By: Seal.Wu
 * Date: 2015/8/20
 * Time: 14:24
 */
public class AESEncryptUtil {

    private static final String ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    private static final String DEFAULT_IV = "com.dahanis.foundation";

    private static final String DEFAULT_KEY = "com.dahanis.foundation";

    private String key;

    private String iv;

    private String algorithm;

    private static final AESEncryptUtil instance = new AESEncryptUtil();

    private AESEncryptUtil() {
        key = DEFAULT_KEY.substring(0, 16);
        iv = DEFAULT_IV.substring(0, 16);
        algorithm = ALGORITHM_CBC;
    }

    private AESEncryptUtil(String key, String iv) {
        this.key = key;
        this.iv = iv;
        algorithm = ALGORITHM_CBC;
    }

    private AESEncryptUtil(String key) {
        this.key = key;
        algorithm = ALGORITHM_ECB;
    }


    /**
     * 获取默认的加密对象
     */
    public static AESEncryptUtil getInstance() {
        return instance;
    }

    /**
     * 获取默认的加密对象
     */
    public static AESEncryptUtil getInstance(String key) {
        return new AESEncryptUtil(key);
    }

    /**
     * 获取定制的加密对象
     *
     * @param key 加密密钥
     * @return 加密对象
     */
    public static AESEncryptUtil getInstance(String key, String iv) {
        return new AESEncryptUtil(key, iv);
    }

    /**
     * 加密
     */
    public String encrypt(String chars) {
        return doAction(chars, true);
    }

    /**
     * 解密
     */
    public String decrypt(String chars) {
        return doAction(chars, false);
    }

    private String doAction(String chars, boolean isEncrypt) {
        if (chars == null) {
            chars = "";
        }
        byte[] bytes;
        try {
            if (!isEncrypt) {
                bytes = Base64.decode(chars);
            } else {
                bytes = chars.getBytes("UTF-8");
            }
            bytes = getWorkedBytes(isEncrypt, key, bytes);
            if (isEncrypt) {
                return Base64.encode(bytes);
            } else {
                return new String(bytes, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    private byte[] getWorkedBytes(boolean isEncrypt, String key, byte[] input) {

        if (null == input || 0 == input.length) {
            return new byte[]{};
        }


        int cryptionMode = isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;

        byte[] outputBytes;

        try {

            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(algorithm);
            if (algorithm.equals(ALGORITHM_CBC)) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(Charset.forName("utf-8")));
                cipher.init(cryptionMode, skeySpec, ivParameterSpec);
            } else if (algorithm.equals(ALGORITHM_ECB)) {
                cipher.init(cryptionMode, skeySpec);
            }
            if (isEncrypt) {
                outputBytes = cipher.doFinal(input);
                return outputBytes;
            }
            return cipher.doFinal(input);
        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (!isEncrypt) {
                    System.out.println(Base64.encode(input));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return new byte[]{};
    }
}
