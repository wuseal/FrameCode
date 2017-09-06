package com.dh.foundation.utils;

/**
 * Created By: Seal.Wu
 * Date: 2017/9/6
 * Time: 15:41
 */
public class AESEncryptUtilTest {

    private String toBeIncreyptString;
    private static String encryptString;
    private String resultString;
    @org.junit.Before
    public void setUp() throws Exception {
        toBeIncreyptString = "hello ,How are you !";
    }

    @org.junit.After
    public void tearDown() throws Exception {
        System.out.println("resultString is: "+resultString);
    }

    @org.junit.Test
    public void encrypt() throws Exception {

        resultString = encryptString = AESEncryptUtil.getInstance().encrypt(toBeIncreyptString);
    }

    @org.junit.Test
    public void decrypt() throws Exception {
        resultString = AESEncryptUtil.getInstance().decrypt(encryptString);
    }

}