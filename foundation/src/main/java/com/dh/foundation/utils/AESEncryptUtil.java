package com.dh.foundation.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 * Created By: Seal.Wu
 * Date: 2015/8/20
 * Time: 14:24
 */
public class AESEncryptUtil {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final String DEFAULT_KEY = "com.dahanis.foundation";

    private String key;

    private static final AESEncryptUtil instance = new AESEncryptUtil();

    private AESEncryptUtil() {
        key = StringUtils.substring(DEFAULT_KEY, 0, 16);
    }

    private AESEncryptUtil(String key) {
        this.key = key;
    }


    /**
     * 获取默认的加密对象
     */
    public static AESEncryptUtil getInstance() {
        return instance;
    }

    /**
     * 获取定制的加密对象
     *
     * @param key 加密密钥
     * @return 加密对象
     */
    public static AESEncryptUtil getInstance(String key) {
        return new AESEncryptUtil(key);
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
        } catch (UnsupportedEncodingException e) {
            DLoggerUtils.e(e);
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
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AESEncryptUtil.ALGORITHM);
            Cipher cipher = Cipher.getInstance(AESEncryptUtil.ALGORITHM);
            cipher.init(cryptionMode, skeySpec);
            if (isEncrypt) {
                outputBytes = cipher.doFinal(input);
                return outputBytes;
            }
            return cipher.doFinal(input);
        } catch (Exception e) {
            DLoggerUtils.e(e);
        }
        return new byte[]{};
    }
}
