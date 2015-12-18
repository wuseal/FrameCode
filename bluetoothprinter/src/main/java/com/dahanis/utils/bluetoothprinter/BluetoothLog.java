package com.dahanis.utils.bluetoothprinter;

import android.util.Log;

import java.util.logging.Level;

/**
 * 蓝牙打印日志输出
 * Created By: Seal.Wu
 * Date: 2015/12/18
 * Time: 14:56
 */
public class BluetoothLog {

    private static Level level = Level.ALL;

    private static final String TAG = "BluetoothPrinter";


   static void i(String message) {

       if (level.intValue() <= Level.INFO.intValue()) {

           Log.i(TAG, message);
       }

   }
   static void e(Throwable throwable) {

       if (level.intValue() <= Level.WARNING.intValue()) {

           Log.i(TAG, throwable.getMessage(), throwable);
       }

   }

    public static void setLevel(Level level) {
        BluetoothLog.level = level;
    }
}
