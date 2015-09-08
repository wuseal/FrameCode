package com.dh.foundation.utils;

import android.annotation.TargetApi;
import android.os.Build;

import com.dh.foundation.manager.FoundationManager;

import java.util.Set;

/**
 * Created By: Seal.Wu
 * Date: 2015/4/14
 * Time: 17:47
 */
public class SharedPreferenceUtils {

    public final static Controller<String> STRING_CONTROLLER = new Controller<String>() {
        @Override
        public void set(String key, String value) {
            FoundationManager.getSharedPreferences().edit().putString(key, value).apply();
        }

        @Override
        public String get(String key, String defaultValue) {
            return FoundationManager.getSharedPreferences().getString(key, defaultValue);
        }
    };

    public final static Controller<Integer> INTEGER_CONTROLLER = new Controller<Integer>() {
        @Override
        public void set(String key, Integer integer) {
            FoundationManager.getSharedPreferences().edit().putInt(key, integer).apply();
        }

        @Override
        public Integer get(String key, Integer defaultValue) {
            return FoundationManager.getSharedPreferences().getInt(key, defaultValue);
        }
    };

    public static final Controller<Boolean> BOOLEAN_CONTROLLER = new Controller<Boolean>() {
        @Override
        public void set(String key, Boolean aBoolean) {
            FoundationManager.getSharedPreferences().edit().putBoolean(key, aBoolean).apply();
        }

        @Override
        public Boolean get(String key, Boolean defaultValue) {
            return FoundationManager.getSharedPreferences().getBoolean(key, defaultValue);
        }
    };

    public static final Controller<Float> FLOAT_CONTROLLER = new Controller<Float>() {
        @Override
        public void set(String key, Float aFloat) {
            FoundationManager.getSharedPreferences().edit().putFloat(key, aFloat).apply();

        }

        @Override
        public Float get(String key, Float defaultValue) {
            return FoundationManager.getSharedPreferences().getFloat(key, defaultValue);
        }
    };
    public static final Controller<Long> LONG_CONTROLLER = new Controller<Long>() {
        @Override
        public void set(String key, Long aLong) {
            FoundationManager.getSharedPreferences().edit().putLong(key, aLong).apply();

        }

        @Override
        public Long get(String key, Long defaultValue) {
            return FoundationManager.getSharedPreferences().getLong(key, defaultValue);
        }
    };
    public static final Controller<Set<String>> STRING_SET_CONTROLLER = new Controller<Set<String>>() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void set(String key, Set<String> aStringSet) {
            FoundationManager.getSharedPreferences().edit().putStringSet(key, aStringSet).apply();

        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public Set<String> get(String key, Set<String> defaultValue) {
            return FoundationManager.getSharedPreferences().getStringSet(key, defaultValue);
        }
    };

    public interface Controller<T> {
        void set(String key, T t);

        T get(String key, T defaultValue);
    }
}
