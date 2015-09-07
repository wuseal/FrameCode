package com.dh.foundation.manager;

import com.dh.foundation.utils.DLoggerUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/14
 * Time: 10:22
 */
public class SingletonInjectFactory {
    private final static SingletonInjectFactory instance = new SingletonInjectFactory();
    private final Map<Class, Class> classClassMap = new HashMap<>();
    private final Map<Class, Object> classObjectMap = new HashMap<>();

    public static SingletonInjectFactory getInstance() {
        return instance;
    }

    private SingletonInjectFactory() {

    }

    public void registerDependencySingletonClass(Class clazz) {
        if (!classClassMap.containsKey(clazz)) {
            classClassMap.put(clazz, clazz);
        }
    }

    public <C, I extends C> void registerDependencySingletonImplementClass(Class<C> clazz, Class<I> implementClazz) {
        if (!classClassMap.containsKey(clazz)) {
            classClassMap.put(clazz, implementClazz);
        }
    }

    public <C, I extends C> void registerDependencySingletonImplementObject(Class<C> clazz, I implementObject) {
        if (!classClassMap.containsKey(clazz)) {
            classClassMap.put(clazz, implementObject.getClass());
            classObjectMap.put(clazz, implementObject);
        }
    }

    public <T> void registerDependencySingletonObject(Class<T> clazz, T object) {
        if (!classClassMap.containsKey(clazz)) {
            classClassMap.put(clazz, object.getClass());
            classObjectMap.put(clazz, object);
        }
    }

    public <T> T getObjectByClass(Class<T> clazz) {
        if (classClassMap.containsKey(clazz)) {
            if (classObjectMap.containsKey(clazz)) {
                return (T) classObjectMap.get(clazz);
            } else {
                try {
                    Object value = clazz.newInstance();
                    classObjectMap.put(clazz, value);
                    return (T) value;
                } catch (InstantiationException e) {
                    DLoggerUtils.e(e);
                } catch (IllegalAccessException e) {
                    DLoggerUtils.e(e);
                }
            }
        } else {
            try {
                Object value = clazz.newInstance();
                return (T) value;
            } catch (InstantiationException e) {
                DLoggerUtils.e(e);
            } catch (IllegalAccessException e) {
                DLoggerUtils.e(e);
            }
        }
        return null;
    }
}
