package com.dh.foundation.utils.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * 打印监听回调接口
 * Created By: Seal.Wu
 * Date: 2015/6/4
 * Time: 17:00
 */
public interface OnPrintListener {
    /**
     * 接收到所有可用的蓝牙设备
     *
     * @param isOnlyBoundedDevices 是否仅仅是已经绑定的设备，已经配对的设备，当第一次获取到设备的时候是已经绑定的默认设备集合
     * @param allAvailableDevices  所有可用的设备，包括已经配对设备（已经配对设备中也有可能有不可用的，因为仅仅是之前配对过的记录）
     */
    void onReceiveDevices(boolean isOnlyBoundedDevices, List<BluetoothDevice> allAvailableDevices);

    /**
     * 初始化蓝牙功能，并获取默认已经配对设备
     */
    void onInitBluetoothFunction();

    /**
     * 初始化蓝牙失败
     */
    void onInitBluetoothFunctionError();

    /**
     * 开始连接蓝牙打印机时调用
     */
    void onStartConnectDevice();

    /**
     * 蓝牙连接设备已经连接成功时调用
     */
    void onConnectedDevice();

    /**
     * 　连接失败时调用
     *
     * @param throwable
     */
    void onConnectedError(Throwable throwable);

    /**
     * 开始打印时调用
     */
    void onStartPrint();

    /**
     * 打印成功时调用
     */
    void onSuccessPrint();

    /**
     * 打印出现异常时调用
     *
     * @param throwable
     */
    void onPrintError(Throwable throwable);


    public static class SimpleOnPrintListener  implements OnPrintListener {


        @Override
        public void onReceiveDevices(boolean isOnlyBoundedDevices, List<BluetoothDevice> allAvailableDevices) {

        }

        @Override
        public void onInitBluetoothFunction() {

        }

        @Override
        public void onInitBluetoothFunctionError() {

        }

        @Override
        public void onStartConnectDevice() {

        }

        @Override
        public void onConnectedDevice() {

        }

        @Override
        public void onConnectedError(Throwable throwable) {

        }

        @Override
        public void onStartPrint() {

        }

        @Override
        public void onSuccessPrint() {

        }

        @Override
        public void onPrintError(Throwable throwable) {

        }
    }
}


