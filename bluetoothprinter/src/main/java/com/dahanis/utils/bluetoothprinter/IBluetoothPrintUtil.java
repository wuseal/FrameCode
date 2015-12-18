package com.dahanis.utils.bluetoothprinter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * 蓝牙打印工具接口
 * Created By: Seal.Wu
 * Date: 2015/6/15
 * Time: 16:06
 */
public interface IBluetoothPrintUtil {
    /**
     * 设置蓝牙打印交互过程监听器
     *
     * @param onPrintListener 交互过程监听器
     */
    void setOnPrintListener(OnPrintListener onPrintListener);

    /**
     * 当前是否已经有设备已经连接到打印机
     *
     * @return
     */
    boolean isConnected();

    /**
     * 开始连接设备
     *
     * @param bluetoothDevice 蓝牙打印设备
     */
    void connectDevice(BluetoothDevice bluetoothDevice);

    /**
     * 打印已经设置构建的数据数据
     */
    void printData();

    /**
     * 一键开启执行蓝牙打印功能模块
     *
     * @param context 要开启打印打印交互的activity
     */
    void print(Context context);

    /**
     * 展示可用蓝牙设备
     *
     * @param context 要开启打印打印交互的activity
     */
    void showDevices(Context context);

    /**
     * 刷新搜寻新的蓝牙设备
     */
    void refreshDevice();

    /**
     * 关闭蓝牙
     */
    void closeBluetooth();

    /**
     * 当离开当前activity时请调用此方法，不然可能会出异常
     */
    void leaveCurrentActivity();

    /**
     * 断开蓝牙连接，并复位蓝牙状态为未初始化状态，不清空内部数据
     */
    void finishBluetoothConnection();

    /**
     * 结束蓝牙打印模块，并清空内部所有信息数据
     */
    void finishBluetoothFunction();


    /**
     * 添加待打印的数据
     *
     * @param printDataBuilder 数据构建器
     */
    void addPrintDataBuilders(DhPrintDataBuilder printDataBuilder);

    /**
     * 清空所有已经添加的打印数据构建器
     */
    void cleanPrintDataBuilders();

}
