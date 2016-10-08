package com.dh.foundation.utils;

import java.lang.reflect.Field;

/**
 * java反射工具类
 * Created By: Seal.Wu
 * Date: 2016/5/6
 * Time: 14:58
 */
public class ReflectUtils {

    /**
     * 获取类对象的字段对象(包含其所有的父类)
     *
     * @param tClass    要获取于哪个类
     * @param fieldName 字段名称
     * @param <T>       类型参数
     * @return 字段对象
     */
    public static <T> Field getDeclaredField(Class<T> tClass, String fieldName) {

        try {
            return tClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            final Class<? super T> superclass = tClass.getSuperclass();
            if (superclass == Object.class) {
                DLoggerUtils.e(e);
                return null;
            } else {
                return getDeclaredField(superclass, fieldName);
            }
        }
    }
}
