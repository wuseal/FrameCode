package com.dh.foundation.utils;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/21
 * Time: 11:42
 */
public class NumUtils {

    public static boolean isInteger(String test) {
        try {
            Integer.parseInt(test);
        } catch (NumberFormatException e) {
          return false;
        }
        return  true;
    }
}
